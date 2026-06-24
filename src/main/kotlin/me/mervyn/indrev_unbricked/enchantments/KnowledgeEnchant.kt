package me.mervyn.indrev_unbricked.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class KnowledgeEnchant : Enchantment(
    Rarity.RARE,
    EnchantmentTarget.WEAPON,
    arrayOf(EquipmentSlot.MAINHAND)
) {
    override fun getMinPower(level: Int): Int = 55 + (level - 1) * 45

    override fun getMaxPower(level: Int): Int = 200

    override fun getMaxLevel(): Int = 3

    override fun getName(level: Int): Text {
        return super.getName(level).copy().formatted(Formatting.DARK_GREEN)
    }
}
