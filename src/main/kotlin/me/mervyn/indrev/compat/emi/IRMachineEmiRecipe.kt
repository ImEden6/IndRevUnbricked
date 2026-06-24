package me.mervyn.indrev.compat.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import me.mervyn.indrev.recipes.machines.IRFluidRecipe
import me.mervyn.indrev.recipes.machines.IRRecipe
import me.mervyn.indrev.utils.IRFluidAmount
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class IRMachineEmiRecipe(
    private val recipe: IRRecipe,
    private val category: EmiRecipeCategory
) : EmiRecipe {

    private val cachedInputs: List<EmiIngredient> by lazy {
        val list = mutableListOf<EmiIngredient>()
        if (recipe is IRFluidRecipe) {
            recipe.fluidInput.forEach { fluid ->
                list.add(fluidToIngredient(fluid))
            }
        }
        list.addAll(recipe.input.map { (ingredient, count) ->
            EmiIngredient.of(ingredient, count.toLong())
        })
        list
    }

    private val cachedOutputs: List<EmiStack> by lazy {
        val list = mutableListOf<EmiStack>()
        list.addAll(recipe.outputs.map { (stack, chance) ->
            EmiStack.of(stack).setChance(chance.toFloat())
        })
        if (recipe is IRFluidRecipe) {
            recipe.fluidOutput.forEach { fluid ->
                list.add(fluidToStack(fluid))
            }
        }
        list
    }

    override fun getCategory(): EmiRecipeCategory = category
    override fun getId(): Identifier? = recipe.id
    override fun getInputs(): List<EmiIngredient> = cachedInputs
    override fun getOutputs(): List<EmiStack> = cachedOutputs
    override fun getDisplayWidth(): Int = 134
    override fun getDisplayHeight(): Int = 66

    override fun addWidgets(widgets: WidgetHolder) {
        val startX = (widgets.width - 134) / 2
        val startY = (widgets.height - 66) / 2

        if (recipe is IRFluidRecipe) {
            if (recipe.fluidInput.isNotEmpty() && recipe.fluidInput[0].amount() > 0) {
                val ingredient = fluidToIngredient(recipe.fluidInput[0])
                widgets.addTank(ingredient, startX - 2, startY, 16, 32, 16000)
            }
            if (recipe.fluidOutput.isNotEmpty() && recipe.fluidOutput[0].amount() > 0) {
                val ingredient = fluidToIngredient(recipe.fluidOutput[0])
                widgets.addTank(ingredient, startX + 83, startY, 16, 32, 16000)
            }
        }

        val itemInputs = cachedInputs.filterNot { it is EmiStack && it.key is net.minecraft.fluid.Fluid }
        if (itemInputs.isNotEmpty()) {
            widgets.addSlot(itemInputs[0], startX + 1, startY + 19)
            if (itemInputs.size > 1) {
                widgets.addSlot(itemInputs[1], startX - 17, startY + 19)
            }
        }

        widgets.addFillingArrow(startX + 24, startY + 18, recipe.ticks * 50)

        val itemOutputs = cachedOutputs.filterNot { it.key is net.minecraft.fluid.Fluid }
        if (itemOutputs.isNotEmpty()) {
            widgets.addSlot(itemOutputs[0], startX + 61, startY + 19)
                .recipeContext(this)
                .drawBack(false)
        }
    }

    override fun supportsRecipeTree(): Boolean = true

    private fun fluidToIngredient(fluid: IRFluidAmount): EmiStack {
        return EmiStack.of(fluid.resource.fluid, fluid.amount())
    }

    private fun fluidToStack(fluid: IRFluidAmount): EmiStack {
        return EmiStack.of(fluid.resource.fluid, fluid.amount())
    }
}
