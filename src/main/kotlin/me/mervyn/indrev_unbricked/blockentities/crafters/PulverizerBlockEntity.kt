package me.mervyn.indrev_unbricked.blockentities.crafters

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.TemperatureComponent
import me.mervyn.indrev_unbricked.components.trackObject
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.recipes.machines.IRRecipeType
import me.mervyn.indrev_unbricked.recipes.machines.PulverizerRecipe
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class PulverizerBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<PulverizerRecipe>(tier, MachineRegistry.PULVERIZER_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.06, 700..1100, 1400)
        this.enhancerComponent = EnhancerComponent(intArrayOf(5, 6, 7, 8), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input { slot = 2 }
            output { slots = intArrayOf(3, 4) }
        }

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])
    }

    override val type: IRRecipeType<PulverizerRecipe> = PulverizerRecipe.TYPE

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
    }
}