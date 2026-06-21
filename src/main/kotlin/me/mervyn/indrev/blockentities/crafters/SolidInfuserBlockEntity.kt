package me.mervyn.indrev.blockentities.crafters

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.api.machines.TransferMode
import me.mervyn.indrev.api.sideconfigs.ConfigurationType
import me.mervyn.indrev.components.EnhancerComponent
import me.mervyn.indrev.components.TemperatureComponent
import me.mervyn.indrev.components.trackObject
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.recipes.machines.IRRecipeType
import me.mervyn.indrev.recipes.machines.InfuserRecipe
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import me.mervyn.indrev.utils.getRecipes
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class SolidInfuserBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    CraftingMachineBlockEntity<InfuserRecipe>(tier, MachineRegistry.SOLID_INFUSER_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.06, 700..1100, 1400)
        this.enhancerComponent = EnhancerComponent(intArrayOf(5, 6, 7, 8), Enhancer.DEFAULT, this::getMaxCount)
        this.inventoryComponent = inventory(this) {
            input {
                slots = intArrayOf(2, 3)
                filter { stack, dir, slot -> canInput(dir, slot, stack) }
            }
            output { slot = 4 }
        }

        trackObject(CRAFTING_COMPONENT_ID, craftingComponents[0])
    }

    private fun canInput(side: Direction?, slot: Int, stack: ItemStack): Boolean {
        if (stack.isEmpty) return true
        if (side != null) {
            val isFirstSlot = when (inventoryComponent!!.itemConfig[side]) {
                TransferMode.INPUT_FIRST -> slot == 2
                TransferMode.INPUT_SECOND -> slot == 3
                else -> true
            }
            if (!isFirstSlot) return false
        }
        val world = world ?: return true
        val recipes = world.recipeManager.getRecipes(InfuserRecipe.TYPE).values
        return when (slot) {
            2 -> recipes.any { recipe -> recipe.input.isNotEmpty() && recipe.input[0].ingredient.test(stack) }
            3 -> recipes.any { recipe -> recipe.input.size > 1 && recipe.input[1].ingredient.test(stack) }
            else -> true
        }
    }

    override fun getValidConfigurations(type: ConfigurationType): Array<TransferMode> {
        return when (type) {
            ConfigurationType.ITEM -> TransferMode.SOLID_INFUSER
            else -> super.getValidConfigurations(type)
        }
    }

    override val type: IRRecipeType<InfuserRecipe> = InfuserRecipe.TYPE

    companion object {
        const val CRAFTING_COMPONENT_ID = 4
    }
}