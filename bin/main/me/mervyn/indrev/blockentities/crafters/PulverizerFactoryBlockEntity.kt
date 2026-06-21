package me.mervyn.indrev.blockentities.crafters

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.components.CraftingComponent
import me.mervyn.indrev.components.EnhancerComponent
import me.mervyn.indrev.components.TemperatureComponent
import me.mervyn.indrev.components.multiblock.MultiBlockComponent
import me.mervyn.indrev.components.multiblock.definitions.FactoryStructureDefinition
import me.mervyn.indrev.components.trackObject
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.recipes.machines.IRRecipeType
import me.mervyn.indrev.recipes.machines.PulverizerRecipe
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class PulverizerFactoryBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<PulverizerRecipe>(tier, MachineRegistry.PULVERIZER_FACTORY_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.06, 700..1100, 1400)
        this.enhancerComponent = EnhancerComponent(intArrayOf(2, 3, 4, 5), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input { slots = intArrayOf(6, 8, 10, 12, 14) }
            output { slots = intArrayOf(7, 9, 11, 13, 15) }
        }
        this.craftingComponents = Array(5) { index ->
            val component = CraftingComponent(index, this).apply {
                inputSlots = intArrayOf(6 + (index * 2))
                outputSlots = intArrayOf(6 + (index * 2) + 1)
            }
            trackObject(CRAFTING_COMPONENT_START_ID + index, component)
            component
        }
        this.multiblockComponent = MultiBlockComponent(FactoryStructureDefinition.SELECTOR)
    }

    override val syncToWorld: Boolean = true

    override val type: IRRecipeType<PulverizerRecipe> = PulverizerRecipe.TYPE

    override fun fromClientTag(tag: NbtCompound) {
        multiblockComponent?.readNbt(tag)
    }

    override fun toClientTag(tag: NbtCompound) {
        multiblockComponent?.writeNbt(tag)
    }

    companion object {
        const val CRAFTING_COMPONENT_START_ID = 4
    }
}