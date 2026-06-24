package me.mervyn.indrev_unbricked.items.armor

import me.mervyn.indrev_unbricked.tools.modular.Module
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.option.KeyBinding
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import me.mervyn.indrev_unbricked.utils.literal
import net.minecraft.text.Text
import me.mervyn.indrev_unbricked.utils.translatable
import net.minecraft.util.Formatting
import net.minecraft.world.World

open class IRModuleItem(val module: Module, settings: Settings) : Item(settings) {

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        if (Screen.hasShiftDown()) {
            module.getTooltip(stack, tooltip)
        } else {
            tooltip?.add(translatable("gui.indrev_unbricked.tooltip.press_shift", literal("").append(KeyBinding.getLocalizedName("key.keyboard.left.shift").get()).formatted(Formatting.BLUE, Formatting.ITALIC)).formatted(Formatting.BLUE, Formatting.ITALIC))
        }
    }
}