package me.mervyn.indrev_unbricked.blockentities.crafters

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.api.machines.TransferMode
import me.mervyn.indrev_unbricked.api.sideconfigs.ConfigurationType
import me.mervyn.indrev_unbricked.blocks.machine.MachineBlock
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.FluidComponent
import me.mervyn.indrev_unbricked.components.trackObject
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.recipes.machines.CondenserRecipe
import me.mervyn.indrev_unbricked.recipes.machines.IRRecipeType
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import me.mervyn.indrev_unbricked.utils.bucket
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class CondenserBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<CondenserRecipe>(tier, MachineRegistry.CONDENSER_REGISTRY, pos, state) {

    init {
        this.enhancerComponent = EnhancerComponent(intArrayOf(3, 4, 5, 6), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            output { slot = 2 }
            coolerSlot = 1
        }
        this.fluidComponent = object : FluidComponent({ this }, bucket * 8) {
            init {
                this.inputTanks = intArrayOf(0)
            }
        }

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])

        trackObject(INPUT_TANK_ID, fluidComponent!![0])
    }

    override val syncToWorld: Boolean = true

    override val type: IRRecipeType<CondenserRecipe> = CondenserRecipe.TYPE

    override fun getMaxCount(enhancer: Enhancer): Int {
        return if (enhancer == Enhancer.SPEED) 4 else super.getMaxCount(enhancer)
    }

    override fun applyDefault(
        state: BlockState,
        type: ConfigurationType,
        configuration: MutableMap<Direction, TransferMode>
    ) {
        val direction = (state.block as MachineBlock).getFacing(state)
        when (type) {
            ConfigurationType.ITEM -> {
                configuration[direction.rotateYCounterclockwise()] = TransferMode.OUTPUT
            }
            else -> super.applyDefault(state, type, configuration)
        }
    }

    override fun getValidConfigurations(type: ConfigurationType): Array<TransferMode> {
        return when (type) {
            ConfigurationType.ITEM -> arrayOf(TransferMode.OUTPUT, TransferMode.NONE)
            else -> return super.getValidConfigurations(type)
        }
    }

    override fun fromClientTag(tag: NbtCompound) {
        fluidComponent!!.fromTag(tag)
    }

    override fun toClientTag(tag: NbtCompound) {
        fluidComponent!!.toTag(tag)
    }


    companion object {
        const val CRAFTING_COMPONENT_ID = 2
        const val INPUT_TANK_ID = 3
    }
}