package me.mervyn.indrev_unbricked.gui.tooltip.modular

import me.mervyn.indrev_unbricked.gui.tooltip.energy.EnergyTooltipData
import me.mervyn.indrev_unbricked.tools.modular.Module

class ModularTooltipData(energy: Long, maxEnergy: Long, val modules: List<Module>, val levelProvider: (Module) -> Int) : EnergyTooltipData(energy, maxEnergy) {
}