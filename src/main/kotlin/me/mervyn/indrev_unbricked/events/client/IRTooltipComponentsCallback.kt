package me.mervyn.indrev_unbricked.events.client

import me.mervyn.indrev_unbricked.gui.tooltip.energy.EnergyTooltipComponent
import me.mervyn.indrev_unbricked.gui.tooltip.energy.EnergyTooltipData
import me.mervyn.indrev_unbricked.gui.tooltip.modular.ModularTooltipComponent
import me.mervyn.indrev_unbricked.gui.tooltip.modular.ModularTooltipData
import me.mervyn.indrev_unbricked.gui.tooltip.oredatacards.OreDataCardTooltipComponent
import me.mervyn.indrev_unbricked.gui.tooltip.oredatacards.OreDataCardTooltipData
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData

object IRTooltipComponentsCallback : TooltipComponentCallback {
    override fun getComponent(data: TooltipData?): TooltipComponent? {
        return when (data) {
            is ModularTooltipData -> ModularTooltipComponent(data)
            is EnergyTooltipData -> EnergyTooltipComponent(data)
            is OreDataCardTooltipData -> OreDataCardTooltipComponent(data)
            else -> null
        }
    }
}