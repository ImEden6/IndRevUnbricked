package me.mervyn.indrev.blockentities.farms

import me.mervyn.indrev.IndustrialRevolution
import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.components.EnhancerComponent
import me.mervyn.indrev.config.BasicMachineConfig
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.registry.MachineRegistry
import me.mervyn.indrev.utils.redirectDrops
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

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