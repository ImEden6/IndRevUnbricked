package me.mervyn.indrev_unbricked.blocks.machine

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.config.IRConfig
import me.mervyn.indrev_unbricked.gui.screenhandlers.machines.ElectrolyticSeparatorScreenHandler
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings

class ElectrolyticSeparatorBlock(registry: MachineRegistry, settings: FabricBlockSettings, tier: Tier)
    : HorizontalFacingMachineBlock(registry, settings, tier,
    when (tier) {
        Tier.MK1 -> IRConfig.machines.electrolyticSeparatorMk1
        Tier.MK2 -> IRConfig.machines.electrolyticSeparatorMk2
        Tier.MK3 -> IRConfig.machines.electrolyticSeparatorMk3
        else -> IRConfig.machines.electrolyticSeparatorMk4
                },
    ::ElectrolyticSeparatorScreenHandler
) {
}