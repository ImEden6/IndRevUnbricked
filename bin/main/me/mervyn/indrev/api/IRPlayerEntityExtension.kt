package me.mervyn.indrev.api

import me.mervyn.indrev.tools.modular.ArmorModule

interface IRPlayerEntityExtension {

    var shieldDurability: Double
    var isRegenerating: Boolean

    fun getMaxShieldDurability(): Double

    fun getAppliedModules(): Map<ArmorModule, Int>
    fun applyModule(module: ArmorModule, level: Int)
    fun isApplied(module: ArmorModule): Boolean
    fun getAppliedLevel(module: ArmorModule): Int
}