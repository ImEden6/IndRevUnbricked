package me.mervyn.indrev.blockentities.crafters

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.components.EnhancerComponent
import me.mervyn.indrev.components.TemperatureComponent
import me.mervyn.indrev.components.trackObject
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.mixin.common.MixinAbstractCookingRecipe
import me.mervyn.indrev.recipes.IRecipeGetter
import me.mervyn.indrev.recipes.machines.VanillaCookingRecipeCachedGetter
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class ElectricFurnaceBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<MixinAbstractCookingRecipe>(tier, MachineRegistry.ELECTRIC_FURNACE_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.1, 1300..1700, 2000)
        this.enhancerComponent = EnhancerComponent(intArrayOf(4, 5, 6, 7), Enhancer.FURNACE, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input { slot = 2 }
            output { slot = 3 }
        }
        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])
    }

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

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
    }
}