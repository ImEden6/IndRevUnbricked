package me.mervyn.indrev_unbricked.blockentities.farms

import me.mervyn.indrev_unbricked.IndustrialRevolution
import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.EnhancerComponent
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
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

private val zenithScavenger by lazy { Registries.ENCHANTMENT.get(Identifier("zenith", "scavenger")) }
private val zenithKnowledge by lazy { Registries.ENCHANTMENT.get(Identifier("zenith", "knowledge")) }

class SlaughterBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) : AOEMachineBlockEntity<BasicMachineConfig>(tier, MachineRegistry.SLAUGHTER_REGISTRY, pos, state) {

    init {
        this.enhancerComponent = EnhancerComponent(
            intArrayOf(11, 12, 13, 14),
            arrayOf(Enhancer.SPEED, Enhancer.BUFFER, Enhancer.DAMAGE),
            this::getMaxCount
        )
        this.inventoryComponent = inventory(this) {
            input { slot = 1 }
            output { slots = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10) }
        }
    }

    override val maxInput: Long = config.maxInput
    override val maxOutput: Long = 0

    var cooldown = 0.0
    override var range = 5

    override fun machineTick() {
        val serverWorld = world as? ServerWorld ?: return
        if (serverWorld.isClient) return
        if (ticks % 15 != 0) return
        val inventory = inventoryComponent?.inventory ?: return
        val enhancers = enhancerComponent!!.enhancers
        cooldown += getProcessingSpeed() * 15
        if (cooldown < config.processSpeed) return
        if (!canUse(getEnergyCost())) {
            workingState = false
            cooldown = 0.0
            return
        }
        val swordStack = inventory.inputSlots.map { inventory.getStack(it) }.firstOrNull { it.item is SwordItem }
        if (swordStack == null || swordStack.isEmpty || swordStack.damage >= swordStack.maxDamage) {
            workingState = false
            cooldown = 0.0
            return
        }
        val fakePlayer = FakePlayer.get(serverWorld)
        val source = serverWorld.damageSources.playerAttack(fakePlayer)
        val mobs = serverWorld.getEntitiesByClass(MobEntity::class.java, getWorkingArea()) { e -> !e.isDead && !e.isInvulnerableTo(source) && (e !is WitherEntity || e.invulnerableTimer <= 0) } ?: emptyList()
        if (mobs.isEmpty()) {
            workingState = false
            cooldown = 0.0
            return
        } else workingState = true
        fakePlayer.inventory.selectedSlot = 0
        val swordItem = swordStack.item as SwordItem
        use(getEnergyCost())

        val rawDamage = (swordItem.attackDamage * Enhancer.getDamageMultiplier(enhancers)).toFloat()
        val dmgSource = serverWorld.damageSources.create(MACHINE_KILL, fakePlayer)

        val useWeaponEnchants = IRConfig.machines.slaughterUseWeaponEnchants

        if (useWeaponEnchants) {
            fakePlayer.setStackInHand(Hand.MAIN_HAND, swordStack.copy())
            val lootingLevel = EnchantmentHelper.getLevel(Enchantments.LOOTING, swordStack)
            val fireAspectLevel = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, swordStack)
            val scavengerLevel = EnchantmentHelper.getLevel(zenithScavenger, swordStack)
            val knowledgeLevel = EnchantmentHelper.getLevel(zenithKnowledge, swordStack)

            mobs.forEach { mob ->
                if (mob.isAlive) {
                    val enchantDamage = EnchantmentHelper.getAttackDamage(swordStack, mob.group)
                    val finalDamage = rawDamage + enchantDamage

                    mob.attacker = fakePlayer

                    val preCounts = inventory.outputSlots.associate { it to inventory.getStack(it).count }

                    mob.redirectDrops(inventory) {
                        if (mob.damage(dmgSource, finalDamage)) {
                            swordStack.damage(1, serverWorld.random, null)
                            if (swordStack.damage >= swordStack.maxDamage) swordStack.decrement(1)
                            if (fireAspectLevel > 0) mob.setOnFireFor(fireAspectLevel * 4)
                        }
                    }

                    if (!mob.isDead) return@forEach

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
                                    serverWorld.spawnEntity(ExperienceOrbEntity(serverWorld, mob.x, mob.y + 0.5, mob.z, xpValue))
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
            }
        } else {
            mobs.forEach { mob ->
                if (mob.isAlive) {
                    mob.redirectDrops(inventory) {
                        if (mob.damage(dmgSource, rawDamage)) {
                            swordStack.damage(1, serverWorld.random, null)
                            if (swordStack.damage >= swordStack.maxDamage) swordStack.decrement(1)
                        }
                    }
                }
            }
        }
        fakePlayer.inventory.clear()
        cooldown = 0.0
    }

    override fun getEnergyCost(): Long {
        val speedEnhancers = (enhancerComponent!!.getCount(Enhancer.SPEED) * 2).coerceAtLeast(1)
        val dmgEnhancers = (enhancerComponent!!.getCount(Enhancer.DAMAGE) * 8).coerceAtLeast(1)
        return config.energyCost * speedEnhancers * dmgEnhancers
    }

    fun getMaxCount(enhancer: Enhancer): Int {
        return when (enhancer) {
            Enhancer.SPEED, Enhancer.DAMAGE -> return 1
            Enhancer.BUFFER -> 4
            else -> 1
        }
    }

    companion object {
        val MACHINE_KILL: RegistryKey<net.minecraft.entity.damage.DamageType> = RegistryKey.of(
            RegistryKeys.DAMAGE_TYPE,
            Identifier(IndustrialRevolution.MOD_ID, "machine_kill")
        )
    }
}
