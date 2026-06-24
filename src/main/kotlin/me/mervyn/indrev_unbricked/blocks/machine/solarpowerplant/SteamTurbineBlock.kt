package me.mervyn.indrev_unbricked.blocks.machine.solarpowerplant

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.blocks.machine.HorizontalFacingMachineBlock
import me.mervyn.indrev_unbricked.config.IRConfig
import me.mervyn.indrev_unbricked.gui.screenhandlers.machines.SteamTurbineScreenHandler
import me.mervyn.indrev_unbricked.registry.MachineRegistry

class SteamTurbineBlock(registry: MachineRegistry, settings: Settings)
    : HorizontalFacingMachineBlock(registry, settings, Tier.MK4, IRConfig.generators.steamTurbine, ::SteamTurbineScreenHandler)