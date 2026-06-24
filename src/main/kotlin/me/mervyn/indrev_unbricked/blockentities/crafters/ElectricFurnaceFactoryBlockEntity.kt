package me.mervyn.indrev_unbricked.blockentities.crafters

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.CraftingComponent
import me.mervyn.indrev_unbricked.components.EnhancerComponent
import me.mervyn.indrev_unbricked.components.TemperatureComponent
import me.mervyn.indrev_unbricked.components.multiblock.MultiBlockComponent
import me.mervyn.indrev_unbricked.components.multiblock.definitions.FactoryStructureDefinition
import me.mervyn.indrev_unbricked.components.trackObject
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.items.upgrade.Enhancer
import me.mervyn.indrev_unbricked.mixin.common.MixinAbstractCookingRecipe
import me.mervyn.indrev_unbricked.recipes.IRecipeGetter
import me.mervyn.indrev_unbricked.recipes.machines.VanillaCookingRecipeCachedGetter
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class ElectricFurnaceFactoryBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<MixinAbstractCookingRecipe>(tier, MachineRegistry.ELECTRIC_FURNACE_FACTORY_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.1, 1300..1700, 2000)
        this.enhancerComponent = EnhancerComponent(intArrayOf(2, 3, 4, 5), Enhancer.FURNACE, this::getMaxCount)
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

    @Suppress("UNCHECKED_CAST")
    override val type: IRecipeGetter<MixinAbstractCookingRecipe>
        get() {
            val upgrades = enhancerComponent!!.enhancers
            return when (upgrades.keys.firstOrNull { it == Enhancer.BLAST_FURNACE || it == Enhancer.SMOKER }) {
                Enhancer.BLAST_FURNACE -> VanillaCookingRecipeCachedGetter.BLASTING
                Enhancer.SMOKER -> VanillaCookingRecipeCachedGetter.SMOKING
                else -> VanillaCookingRecipeCachedGetter.SMELTING
            } as IRecipeGetter<MixinAbstractCookingRecipe>
        }

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