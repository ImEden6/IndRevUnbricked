package me.mervyn.indrev_unbricked.blockentities.farms

import me.mervyn.indrev_unbricked.IndustrialRevolution
import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.autosync
import me.mervyn.indrev_unbricked.config.BasicMachineConfig
import me.mervyn.indrev_unbricked.config.IRConfig
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import me.mervyn.indrev_unbricked.utils.redirectDrops
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.block.BlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.registry.Registries
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

private val zenithScavenger by lazy { Registries.ENCHANTMENT.get(Identifier("zenith", "scavenger")) }
private val zenithKnowledge by lazy { Registries.ENCHANTMENT.get(Identifier("zenith", "knowledge")) }

class RancherBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : AOEMachineBlockEntity<BasicMachineConfig>(tier, MachineRegistry.RANCHER_REGISTRY, pos, state) {

    init {
        this.enhancerComponent = EnhancerComponent(intArrayOf(15, 16, 17, 18), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input { slots = intArrayOf(2, 3, 4, 5) }
            output { slots = intArrayOf(6, 7, 8, 9, 10, 11, 12, 13, 14) }
            coolerSlot = 1
        }
    }

    override val maxInput: Long = config.maxInput
    override val maxOutput: Long = 0

    var cooldown = 0.0
    override var range = 5
    var feedBabies: Boolean by autosync(FEED_BABIES_ID, true)
    var mateAdults: Boolean by autosync(MATE_ADULTS, true)
    var matingLimit: Int by autosync(MATING_LIMIT, 16)
    var killAfter: Int by autosync(KILL_AFTER, 8)

    override fun machineTick() {
        if (world?.isClient == true) return
        val inventory = inventoryComponent?.inventory ?: return
        cooldown += getProcessingSpeed()
        if (cooldown < config.processSpeed) return
        val animals = world?.getEntitiesByClass(AnimalEntity::class.java, getWorkingArea()) { true } ?: emptyList()
        if (animals.isEmpty() || !canUse(getEnergyCost())) {
            workingState = false
            cooldown = 0.0
            return
        } else workingState = true
        val swordStack = inventory.inputSlots.map { inventory.getStack(it) }.firstOrNull { it.item is SwordItem }
        val fakePlayer = FakePlayer.get(world as ServerWorld)
        fakePlayer.inventory.selectedSlot = 0
        val serverWorld = world as ServerWorld
        val dmgSource = serverWorld.damageSources.playerAttack(fakePlayer)
        val useWeaponEnchants = IRConfig.machines.rancherUseWeaponEnchants

        if (swordStack != null && !swordStack.isEmpty && swordStack.damage < swordStack.maxDamage) {
            val swordItem = swordStack.item as SwordItem
            val kill = filterAnimalsToKill(animals)
            if (kill.isNotEmpty()) use(getEnergyCost())

            if (useWeaponEnchants) {
                fakePlayer.setStackInHand(Hand.MAIN_HAND, swordStack.copy())
                val lootingLevel = EnchantmentHelper.getLevel(Enchantments.LOOTING, swordStack)
                val fireAspectLevel = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, swordStack)
                val scavengerLevel = EnchantmentHelper.getLevel(zenithScavenger, swordStack)
                val knowledgeLevel = EnchantmentHelper.getLevel(zenithKnowledge, swordStack)
                val baseDamage = swordItem.attackDamage

                kill.forEach { animal ->
                    if (!animal.isAlive) return@forEach
                    val enchantDamage = EnchantmentHelper.getAttackDamage(swordStack, animal.group)
                    val finalDamage = baseDamage + enchantDamage

                    animal.attacker = fakePlayer

                    val preCounts = inventory.outputSlots.associate { it to inventory.getStack(it).count }

                    animal.redirectDrops(inventory) {
                        if (animal.damage(dmgSource, finalDamage)) {
                            swordStack.damage(1, serverWorld.random, null)
                            if (swordStack.damage >= swordStack.maxDamage) swordStack.decrement(1)
                            if (fireAspectLevel > 0) animal.setOnFireFor(fireAspectLevel * 4)
                        }
                    }

                    if (!animal.isDead) return@forEach

                    if (knowledgeLevel > 0) {
                        inventory.outputSlots.forEach { slot ->
                            val stack = inventory.getStack(slot)
                            val before = preCounts[slot] ?: 0
                            if (stack.count > before) {
                                val diff = stack.count - before
                                stack.count = before
                                var xpTotal = diff * knowledgeLevel * 25
                                while (xpTotal > 0) {
                                    val xpValue = ExperienceOrbEntity.roundToOrbSize(xpTotal)
                                    xpTotal -= xpValue
                                    serverWorld.spawnEntity(ExperienceOrbEntity(serverWorld, animal.x, animal.y + 0.5, animal.z, xpValue))
                                }
                            }
                        }
                    }

                    if (lootingLevel > 0) {
                        inventory.outputSlots.forEach { slot ->
                            val stack = inventory.getStack(slot)
                            val before = preCounts[slot] ?: 0
                            if (stack.count > before) {
                                val extra = serverWorld.random.nextInt(lootingLevel + 1)
                                if (extra > 0) {
                                    inventory.output(ItemStack(stack.item, extra))
                                }
                            }
                        }
                    }

                    if (scavengerLevel > 0 && serverWorld.random.nextInt(100) < (scavengerLevel * 2.5f).toInt()) {
                        inventory.outputSlots.forEach { slot ->
                            val stack = inventory.getStack(slot)
                            val before = preCounts[slot] ?: 0
                            if (stack.count > before) {
                                val diff = stack.count - before
                                if (diff > 0) inventory.output(ItemStack(stack.item, diff))
                            }
                        }
                    }
                }
            } else {
                kill.forEach { animal ->
                    animal.redirectDrops(inventory) {
                        if (!animal.isAlive || !animal.damage(dmgSource, swordItem.attackDamage)) return@forEach
                        swordStack.damage(1, serverWorld.random, null)
                        if (swordStack.damage >= swordStack.maxDamage) swordStack.decrement(1)
                    }
                }
            }
        }
        for (animal in animals) {
            inventory.inputSlots.forEach { slot ->
                val stack = inventory.getStack(slot).copy()
                animal.redirectDrops(inventory) {
                    if (tryFeed(animals.size, animal, inventory.getStack(slot)).isAccepted) return@forEach
                    fakePlayer.inventory.selectedSlot = 8
                    fakePlayer.setStackInHand(Hand.MAIN_HAND, stack)
                    if (animal.interactMob(fakePlayer, Hand.MAIN_HAND).isAccepted)
                        use(getEnergyCost())
                    val inserted = inventory.output(fakePlayer.inventory.getStack(0))
                    val handStack = fakePlayer.getStackInHand(Hand.MAIN_HAND)
                    if (!handStack.isEmpty && handStack.item != stack.item) {
                        inventory.output(handStack)
                        fakePlayer.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY)
                    }
                    if (inserted)
                        inventory.setStack(slot, stack)
                    fakePlayer.inventory.clear()
                }
            }
        }
        fakePlayer.inventory.clear()
        cooldown = 0.0
    }

    private fun tryFeed(size: Int, animalEntity: AnimalEntity, stack: ItemStack): ActionResult {
        val fakePlayer = FakePlayer.get(world as ServerWorld)
        if (animalEntity.isBreedingItem(stack)) {
            val breedingAge: Int = animalEntity.breedingAge
            if (!world!!.isClient && breedingAge == 0 && animalEntity.canEat() && size <= matingLimit && mateAdults) {
                animalEntity.eat(fakePlayer, Hand.MAIN_HAND, stack)
                animalEntity.lovePlayer(fakePlayer)
            }
            if (animalEntity.isBaby && feedBabies) {
                animalEntity.eat(fakePlayer, Hand.MAIN_HAND, stack)
                animalEntity.growUp(((-breedingAge / 20f) * 0.1f).toInt(), true)
            }
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }

    private fun filterAnimalsToKill(entities: List<AnimalEntity>): List<AnimalEntity> {
        val adults = entities.filter { !it.isBaby }
        val types = adults.map { it.type }.associateWith { mutableListOf<AnimalEntity>() }
        adults.forEach { types[it.type]?.add(it) }
        return types.values.let { values ->
            values.map { animals -> animals.dropLast((animals.size - killAfter).coerceAtLeast(killAfter)) }
        }.flatten()
    }

    override fun getEnergyCost(): Long {
        val speedEnhancers = (enhancerComponent!!.getCount(Enhancer.SPEED) * 2).coerceAtLeast(1)
        return config.energyCost * speedEnhancers
    }

    fun getMaxCount(enhancer: Enhancer): Int {
        return when (enhancer) {
            Enhancer.SPEED -> return 1
            Enhancer.BUFFER -> 4
            else -> 1
        }
    }

    override fun toTag(tag: NbtCompound) {
        super.toTag(tag)
        tag.putBoolean("feedBabies", feedBabies)
        tag.putBoolean("mateAdults", mateAdults)
        tag.putInt("matingLimit", matingLimit)
        tag.putInt("killAfter", killAfter)
    }

    override fun fromTag(tag: NbtCompound) {
        super.fromTag(tag)
        feedBabies = tag.getBoolean("feedBabies")
        mateAdults = tag.getBoolean("mateAdults")
        matingLimit = tag.getInt("matingLimit")
        killAfter = tag.getInt("killAfter")
    }

    companion object {
        const val FEED_BABIES_ID = 2
        const val MATE_ADULTS = 3
        const val MATING_LIMIT = 4
        const val KILL_AFTER = 5
    }
}
