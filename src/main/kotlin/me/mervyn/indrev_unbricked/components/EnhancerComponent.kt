package me.mervyn.indrev_unbricked.components

import it.unimi.dsi.fastutil.ints.IntBinaryOperator
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.items.upgrade.IREnhancerItem
import me.mervyn.indrev_unbricked.utils.component1
import me.mervyn.indrev_unbricked.utils.component2
import net.minecraft.inventory.Inventory

open class EnhancerComponent(
    val slots: IntArray,
    val compatible: Array<Enhancer>,
    val maxSlotCount: (Enhancer) -> Int
) {

    val enhancers: Object2IntMap<Enhancer> = Object2IntOpenHashMap()

    fun updateEnhancers(inventory: Inventory) {
        enhancers.clear()
        slots
            .forEach { slot ->
                val (stack, item) = inventory.getStack(slot)
                if (item is IREnhancerItem && compatible.contains(item.enhancer))
                    enhancers.mergeInt(item.enhancer, stack.count, IntBinaryOperator { i, j -> i + j })
            }
    }

    fun getCount(enhancer: Enhancer) = enhancers.getInt(enhancer)

    open fun isLocked(slot: Int, tier: Tier) = slots.indexOf(slot) > tier.ordinal
}