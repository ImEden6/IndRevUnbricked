package me.mervyn.indrev_unbricked.blockentities.farms

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.blockentities.MachineBlockEntity
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.config.BasicMachineConfig
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import me.mervyn.indrev_unbricked.utils.component1
import me.mervyn.indrev_unbricked.utils.component2
import me.mervyn.indrev_unbricked.utils.toVec3d
import net.minecraft.block.BlockState
import net.minecraft.item.FishingRodItem
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class FisherBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : MachineBlockEntity<BasicMachineConfig>(tier, MachineRegistry.FISHER_REGISTRY, pos, state) {

    init {
        this.enhancerComponent = EnhancerComponent(intArrayOf(6, 7, 8, 9), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input {
                slot = 1
                filter { (_, item), _ -> item is FishingRodItem }
            }
            output { slots = intArrayOf(2, 3, 4, 5) }
        }
    }

    private var cooldown = config.processSpeed
    
    override val maxInput: Long = config.maxInput
    override val maxOutput: Long = 0

    override fun machineTick() {
        if (ticks % 15 != 0) return
        cooldown += getProcessingSpeed() * 15
        if (cooldown < config.processSpeed) return
        if (!canUse(getEnergyCost())) return
        val rodStack = inventoryComponent!!.inventory.getStack(1)
        if (rodStack.isEmpty || rodStack.item !is FishingRodItem) return

        val inventory = inventoryComponent?.inventory ?: return
        var hasSpace = false
        for (slot in inventory.outputSlots) {
            val stack = inventory.getStack(slot)
            if (stack.isEmpty || stack.count < stack.maxCount) {
                hasSpace = true
                break
            }
        }
        if (!hasSpace) return

        if (!use(getEnergyCost())) return
        cooldown = 0.0
        var damagedRod = false
        Direction.values().forEach { direction ->
            val pos = pos.offset(direction)
            if (world?.isWater(pos) == true) {
                val identifiers = getIdentifiers(tier)
                val id = identifiers[world!!.random!!.nextInt(identifiers.size)]
                val lootTable = (world as ServerWorld).server.lootManager.getLootTable(id)
                val ctx = LootContext.Builder(LootContextParameterSet.Builder(world as ServerWorld)
                    .add(LootContextParameters.ORIGIN, pos.toVec3d())
                    .add(LootContextParameters.TOOL, rodStack)
                    .build(LootContextTypes.FISHING))
                    .build(null)
                lootTable.generateLoot(ctx) { stack -> inventoryComponent?.inventory?.output(stack) }
                if (!damagedRod) {
                    rodStack?.apply {
                        val isUnbreakable = hasNbt() && nbt?.getBoolean("Unbreakable") == true
                        if (isDamageable && !isUnbreakable) {
                            damage(1, world?.random, null)
                            if (damage >= maxDamage) decrement(1)
                        }
                    }
                    damagedRod = true
                }
            }
        }
    }

    override fun getEnergyCost(): Long {
        val speedEnhancers = (enhancerComponent!!.getCount(Enhancer.SPEED) * 2).coerceAtLeast(1)
        return config.energyCost * speedEnhancers
    }

    private fun getIdentifiers(tier: Tier) = when (tier) {
        Tier.MK2 -> arrayOf(FISH_IDENTIFIER)
        Tier.MK3 -> arrayOf(FISH_IDENTIFIER, FISH_IDENTIFIER, JUNK_IDENTIFIER, JUNK_IDENTIFIER, TREASURE_IDENTIFIER)
        else -> arrayOf(FISH_IDENTIFIER, FISH_IDENTIFIER, FISH_IDENTIFIER, TREASURE_IDENTIFIER)
    }

    fun getMaxCount(enhancer: Enhancer): Int {
        return when (enhancer) {
            Enhancer.SPEED, Enhancer.BUFFER -> 4
            else -> 1
        }
    }

    companion object {
        private val FISH_IDENTIFIER = Identifier("gameplay/fishing/fish")
        private val JUNK_IDENTIFIER = Identifier("gameplay/fishing/junk")
        private val TREASURE_IDENTIFIER = Identifier("gameplay/fishing/treasure")
    }
}