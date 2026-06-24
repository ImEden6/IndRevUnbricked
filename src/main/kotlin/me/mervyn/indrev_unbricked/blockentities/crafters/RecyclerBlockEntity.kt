package me.mervyn.indrev_unbricked.blockentities.crafters

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.trackObject
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.recipes.machines.IRRecipeType
import me.mervyn.indrev_unbricked.recipes.machines.RecyclerRecipe
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class RecyclerBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : CraftingMachineBlockEntity<RecyclerRecipe>(tier, MachineRegistry.RECYCLER_REGISTRY, pos, state) {

    init {
        this.enhancerComponent = object : EnhancerComponent(intArrayOf(4, 5, 6, 7), Enhancer.DEFAULT, this::getMaxCount) {
            override fun isLocked(slot: Int, tier: Tier): Boolean = false
        }

        this.inventoryComponent = inventory(this) {
            input { slot = 2 }
            output { slot = 3 }
            coolerSlot = 1
        }

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])
    }

    override val type: IRRecipeType<RecyclerRecipe> = RecyclerRecipe.TYPE

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
    }
}