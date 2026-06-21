package me.mervyn.indrev.blockentities.crafters

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.api.machines.TransferMode
import me.mervyn.indrev.api.sideconfigs.ConfigurationType
import me.mervyn.indrev.components.EnhancerComponent
import me.mervyn.indrev.components.FluidComponent
import me.mervyn.indrev.components.TemperatureComponent
import me.mervyn.indrev.components.trackObject
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.recipes.machines.ElectrolysisRecipe
import me.mervyn.indrev.recipes.machines.IRRecipeType
import me.mervyn.indrev.registry.MachineRegistry
import me.mervyn.indrev.utils.bucket
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class ElectrolyticSeparatorBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : CraftingMachineBlockEntity<ElectrolysisRecipe>(tier, MachineRegistry.ELECTROLYTIC_SEPARATOR_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.06, 500..700, 900)
        this.enhancerComponent = EnhancerComponent(intArrayOf(1, 2, 3, 4), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            coolerSlot = 0
        }
        this.fluidComponent = ElectrolyticSeparatorFluidComponent()

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])

        trackObject(INPUT_TANK_ID, fluidComponent!![0])
        trackObject(FIRST_OUTPUT_TANK_ID, fluidComponent!![1])
        trackObject(SECOND_OUTPUT_TANK_ID, fluidComponent!![2])
    }

    override val type: IRRecipeType<ElectrolysisRecipe> = ElectrolysisRecipe.TYPE

    override fun applyDefault(
        state: BlockState,
        type: ConfigurationType,
        configuration: MutableMap<Direction, TransferMode>
    ) {
        if (type != ConfigurationType.ITEM)
            super.applyDefault(state, type, configuration)
    }

    override fun getValidConfigurations(type: ConfigurationType): Array<TransferMode> {
        return when (type) {
            ConfigurationType.FLUID -> TransferMode.ELECTROLYTIC_SEPARATOR
            else -> return super.getValidConfigurations(type)
        }
    }

    inner class ElectrolyticSeparatorFluidComponent : FluidComponent({ this }, bucket * 4, 3) {

        init {
            this.inputTanks = intArrayOf(0)
            this.outputTanks = intArrayOf(1, 2)
        }

        override fun getValidTanks(dir: Direction?): IntArray {
            return when (transferConfig[dir]!!) {
                TransferMode.OUTPUT_FIRST -> intArrayOf(1)
                TransferMode.OUTPUT_SECOND -> intArrayOf(2)
                else -> super.getValidTanks(dir)
            }
        }
    }

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
        const val INPUT_TANK_ID = 5
        const val FIRST_OUTPUT_TANK_ID = 6
        const val SECOND_OUTPUT_TANK_ID = 7
    }
}