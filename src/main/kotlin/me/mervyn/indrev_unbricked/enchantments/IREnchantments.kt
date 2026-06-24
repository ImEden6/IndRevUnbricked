package me.mervyn.indrev_unbricked.enchantments

import me.mervyn.indrev_unbricked.IndustrialRevolution
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object IREnchantments {
    val SCAVENGER: Enchantment = register("scavenger", ScavengerEnchant())
    val KNOWLEDGE: Enchantment = register("knowledge", KnowledgeEnchant())

    private fun register(name: String, enchantment: Enchantment): Enchantment {
        return Registry.register(Registries.ENCHANTMENT, Identifier(IndustrialRevolution.MOD_ID, name), enchantment)
    }
}
