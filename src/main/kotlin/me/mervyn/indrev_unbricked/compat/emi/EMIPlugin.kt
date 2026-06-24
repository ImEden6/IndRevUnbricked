package me.mervyn.indrev_unbricked.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiInfoRecipe
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

    private data class Entry<T : IRRecipe>(
        val type: IRRecipeType<T>,
        val registry: MachineRegistry
    )

    private val entries: List<Entry<*>> = listOf(
        Entry(PulverizerRecipe.TYPE, MachineRegistry.PULVERIZER_REGISTRY),
        Entry(InfuserRecipe.TYPE, MachineRegistry.SOLID_INFUSER_REGISTRY),
        Entry(CompressorRecipe.TYPE, MachineRegistry.COMPRESSOR_REGISTRY),
        Entry(RecyclerRecipe.TYPE, MachineRegistry.RECYCLER_REGISTRY),
        Entry(FluidInfuserRecipe.TYPE, MachineRegistry.FLUID_INFUSER_REGISTRY),
        Entry(CondenserRecipe.TYPE, MachineRegistry.CONDENSER_REGISTRY),
        Entry(SmelterRecipe.TYPE, MachineRegistry.SMELTER_REGISTRY),
        Entry(SawmillRecipe.TYPE, MachineRegistry.SAWMILL_REGISTRY),
        Entry(ModuleRecipe.TYPE, MachineRegistry.MODULAR_WORKBENCH_REGISTRY),
        Entry(LaserRecipe.TYPE, MachineRegistry.LASER_EMITTER_REGISTRY),
        Entry(ElectrolysisRecipe.TYPE, MachineRegistry.ELECTROLYTIC_SEPARATOR_REGISTRY),
    )

    override fun register(registry: EmiRegistry) {
        registerCategories(registry)
        registerRecipes(registry)
        registerUpgradeInfo(registry)
        hideItems(registry)
    }

    private fun registerCategories(registry: EmiRegistry) {
        for (entry in entries) {
            val id = Identifier(IndustrialRevolution.MOD_ID, entry.type.id.path)
            val icon = EmiStack.of(entry.registry.block(entry.registry.tiers[0]))
            val category = EmiRecipeCategory(id, icon)
            categories[entry.type.id] = category
            registry.addCategory(category)

            for (tier in entry.registry.tiers) {
                registry.addWorkstation(category, EmiStack.of(entry.registry.block(tier)))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun registerRecipes(registry: EmiRegistry) {
        for (entry in entries) {
            val category = categories[entry.type.id] ?: continue
            val type = entry.type as IRRecipeType<IRRecipe>
            for (recipe in registry.recipeManager.getAllOfType(type).values) {
                if (recipe is IRRecipe) {
                    registry.addRecipe(IRMachineEmiRecipe(recipe, category))
                }
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
                            translatable("indrev_unbricked.category.rei.upgrading",
                                translatable("item.indrev_unbricked.tier_upgrade_" + tier.toString()
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
            stack.id.namespace == IndustrialRevolution.MOD_ID && hide(stack.id)
        }
    }
}
