package me.mervyn.indrev.api

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack

interface CustomEnchantmentProvider {
    fun getLevel(enchantment: Enchantment, itemStack: ItemStack): Int
}