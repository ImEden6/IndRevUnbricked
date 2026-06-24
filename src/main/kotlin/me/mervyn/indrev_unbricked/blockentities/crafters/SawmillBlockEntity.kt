package me.mervyn.indrev_unbricked.blockentities.crafters

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.TemperatureComponent
import me.mervyn.indrev_unbricked.components.trackObject
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.recipes.machines.IRRecipeType
import me.mervyn.indrev_unbricked.recipes.machines.SawmillRecipe
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class SawmillBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : CraftingMachineBlockEntity<SawmillRecipe>(tier, MachineRegistry.SAWMILL_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.06, 700..1100, 1400)
        this.enhancerComponent = EnhancerComponent(intArrayOf(7, 8, 9, 10), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input { slot = 2 }
            output { slots = intArrayOf(3, 4, 5, 6) }
        }

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])
    }

    override val type: IRRecipeType<SawmillRecipe> = SawmillRecipe.TYPE

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
    }
}