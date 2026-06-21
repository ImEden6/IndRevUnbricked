package me.mervyn.indrev.blocks.machine.solarpowerplant

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blocks.machine.HorizontalFacingMachineBlock
import me.mervyn.indrev.config.IRConfig
import me.mervyn.indrev.gui.screenhandlers.machines.SteamTurbineScreenHandler
import me.mervyn.indrev.registry.MachineRegistry

class SteamTurbineBlock(registry: MachineRegistry, settings: Settings)
    : HorizontalFacingMachineBlock(registry, settings, Tier.MK4, IRConfig.generators.steamTurbine, ::SteamTurbineScreenHandler)