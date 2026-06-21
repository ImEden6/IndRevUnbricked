package me.mervyn.indrev.gui.tooltip.modular

import me.mervyn.indrev.gui.tooltip.energy.EnergyTooltipData
import me.mervyn.indrev.tools.modular.Module

class ModularTooltipData(energy: Long, maxEnergy: Long, val modules: List<Module>, val levelProvider: (Module) -> Int) : EnergyTooltipData(energy, maxEnergy) {
}