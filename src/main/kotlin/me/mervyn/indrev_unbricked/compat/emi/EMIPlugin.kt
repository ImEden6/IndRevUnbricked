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

    private val recipeMachineMap = mapOf(
        PulverizerRecipe.IDENTIFIER to MachineRegistry.PULVERIZER_REGISTRY,
        InfuserRecipe.IDENTIFIER to MachineRegistry.SOLID_INFUSER_REGISTRY,
        CompressorRecipe.IDENTIFIER to MachineRegistry.COMPRESSOR_REGISTRY,
        RecyclerRecipe.IDENTIFIER to MachineRegistry.RECYCLER_REGISTRY,
        FluidInfuserRecipe.IDENTIFIER to MachineRegistry.FLUID_INFUSER_REGISTRY,
        CondenserRecipe.IDENTIFIER to MachineRegistry.CONDENSER_REGISTRY,
        SmelterRecipe.IDENTIFIER to MachineRegistry.SMELTER_REGISTRY,
        SawmillRecipe.IDENTIFIER to MachineRegistry.SAWMILL_REGISTRY,
        ModuleRecipe.IDENTIFIER to MachineRegistry.MODULAR_WORKBENCH_REGISTRY,
        LaserRecipe.IDENTIFIER to MachineRegistry.LASER_EMITTER_REGISTRY,
        ElectrolysisRecipe.IDENTIFIER to MachineRegistry.ELECTROLYTIC_SEPARATOR_REGISTRY,
    )

    override fun register(registry: EmiRegistry) {
        registerCategories(registry)
        registerRecipes(registry)
        registerWorkstations(registry)
        registerUpgradeInfo(registry)
        hideItems(registry)
    }

    private fun registerCategories(registry: EmiRegistry) {
        fun add(id: Identifier, icon: ItemStack) {
            val catId = Identifier(IndustrialRevolution.MOD_ID, id.path)
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
        add(LaserRecipe.IDENTIFIER, ItemStack(MachineRegistry.LASER_EMITTER_REGISTRY.block(Tier.MK4)))
        add(ElectrolysisRecipe.IDENTIFIER, ItemStack(MachineRegistry.ELECTROLYTIC_SEPARATOR_REGISTRY.block(Tier.MK4)))
    }

    private fun registerRecipes(registry: EmiRegistry) {
        for (type in registry.recipeManager.recipes.keys) {
            if (type is IRRecipeType<*> && type.id.namespace == IndustrialRevolution.MOD_ID) {
                val category = categories[type.id] ?: continue
                @Suppress("UNCHECKED_CAST")
                val irType = type as IRRecipeType<IRRecipe>
                for (recipe in registry.recipeManager.getAllOfType(irType).values) {
                    if (recipe is IRRecipe) {
                        registry.addRecipe(IRMachineEmiRecipe(recipe, category))
                    }
                }
            }
        }
    }

    private fun registerWorkstations(registry: EmiRegistry) {
        for ((id, machineReg) in recipeMachineMap) {
            val category = categories[id] ?: continue
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
