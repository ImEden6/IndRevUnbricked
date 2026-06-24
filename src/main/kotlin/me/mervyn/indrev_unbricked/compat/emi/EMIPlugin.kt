package me.mervyn.indrev_unbricked.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiInfoRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import me.mervyn.indrev_unbricked.IndustrialRevolution
import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.recipes.machines.*
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import me.mervyn.indrev_unbricked.utils.hide
import me.mervyn.indrev_unbricked.utils.translatable
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

import java.util.*

object EMIPlugin : EmiPlugin {

    private val categories = mutableMapOf<Identifier, EmiRecipeCategory>()

    override fun register(registry: EmiRegistry) {
        registerCategories(registry)
        registerRecipes(registry)
        registerWorkstations(registry)
        registerUpgradeInfo(registry)
        hideItems(registry)
    }

    private fun registerCategories(registry: EmiRegistry) {
        fun add(id: Identifier, icon: ItemStack) {
            val catId = Identifier("indrev_unbricked", id.path)
            val category = EmiRecipeCategory(catId, EmiStack.of(icon))
            categories[id] = category
            registry.addCategory(category)
        }

        add(PulverizerRecipe.IDENTIFIER, ItemStack(MachineRegistry.PULVERIZER_REGISTRY.block(Tier.MK1)))
        add(InfuserRecipe.IDENTIFIER, ItemStack(MachineRegistry.SOLID_INFUSER_REGISTRY.block(Tier.MK1)))
        add(CompressorRecipe.IDENTIFIER, ItemStack(MachineRegistry.COMPRESSOR_REGISTRY.block(Tier.MK1)))
        add(RecyclerRecipe.IDENTIFIER, ItemStack(MachineRegistry.RECYCLER_REGISTRY.block(Tier.MK2)))
        add(FluidInfuserRecipe.IDENTIFIER, ItemStack(MachineRegistry.FLUID_INFUSER_REGISTRY.block(Tier.MK1)))
        add(CondenserRecipe.IDENTIFIER, ItemStack(MachineRegistry.CONDENSER_REGISTRY.block(Tier.MK4)))
        add(SmelterRecipe.IDENTIFIER, ItemStack(MachineRegistry.SMELTER_REGISTRY.block(Tier.MK4)))
        add(SawmillRecipe.IDENTIFIER, ItemStack(MachineRegistry.SAWMILL_REGISTRY.block(Tier.MK4)))
        add(ModuleRecipe.IDENTIFIER, ItemStack(MachineRegistry.MODULAR_WORKBENCH_REGISTRY.block(Tier.MK4)))
    }

    private fun registerRecipes(registry: EmiRegistry) {
        registry.addDeferredRecipes {
            val deferred = mutableListOf<EmiRecipe>()
            for ((typeId, category) in categories) {
                val type = registry.recipeManager.recipes.keys
                    .find { it is IRRecipeType<*> && it.id == typeId } as? IRRecipeType<IRRecipe> ?: continue
                for (recipe in registry.recipeManager.getAllOfType(type).values) {
                    if (recipe is IRRecipe) {
                        deferred.add(IRMachineEmiRecipe(recipe, category))
                    }
                }
            }
            deferred
        }
    }

    private fun registerWorkstations(registry: EmiRegistry) {
        val machineCategoryIds = listOf(
            PulverizerRecipe.IDENTIFIER to MachineRegistry.PULVERIZER_REGISTRY,
            InfuserRecipe.IDENTIFIER to MachineRegistry.SOLID_INFUSER_REGISTRY,
            CompressorRecipe.IDENTIFIER to MachineRegistry.COMPRESSOR_REGISTRY,
            RecyclerRecipe.IDENTIFIER to MachineRegistry.RECYCLER_REGISTRY,
            FluidInfuserRecipe.IDENTIFIER to MachineRegistry.FLUID_INFUSER_REGISTRY,
            CondenserRecipe.IDENTIFIER to MachineRegistry.CONDENSER_REGISTRY,
            SmelterRecipe.IDENTIFIER to MachineRegistry.SMELTER_REGISTRY,
            SawmillRecipe.IDENTIFIER to MachineRegistry.SAWMILL_REGISTRY,
            ModuleRecipe.IDENTIFIER to MachineRegistry.MODULAR_WORKBENCH_REGISTRY
        )

        machineCategoryIds.forEach { (id, machineReg) ->
            val category = categories[id] ?: return@forEach
            machineReg.forEachBlock { _, block ->
                registry.addWorkstation(category, EmiStack.of(block))
            }
        }
    }

    private fun registerUpgradeInfo(registry: EmiRegistry) {
        MachineRegistry.MAP.entries.distinctBy { (_, v) -> v }.forEach { (id, machineReg) ->
            if (machineReg.upgradeable && machineReg.tiers.size > 1) {
                machineReg.forEachBlock { tier, block ->
                    if (tier != Tier.CREATIVE && tier != Tier.MK1) {
                        val stack = EmiStack.of(block)
                        val infoText = listOf<Text>(
                            translatable("indrev.category.rei.upgrading",
                                translatable("item.indrev.tier_upgrade_" + tier.toString()
                                    .lowercase(Locale.getDefault())).formatted(Formatting.DARK_GRAY),
                                translatable(machineReg.block(
                                    machineReg.tiers[machineReg.tiers.indexOf(tier) - 1]
                                ).translationKey).formatted(Formatting.DARK_GRAY),
                                tier.toString())
                        )
                        val recipeId = Identifier(IndustrialRevolution.MOD_ID,
                            "/upgrade_info/${id.path}_${tier.toString().lowercase()}")
                        registry.addRecipe(EmiInfoRecipe(listOf<EmiIngredient>(stack), infoText, recipeId))
                    }
                }
            }
        }
    }

    private fun hideItems(registry: EmiRegistry) {
        registry.removeEmiStacks { stack ->
            stack.id.namespace == "indrev_unbricked" && hide(stack.id)
        }
    }
}
