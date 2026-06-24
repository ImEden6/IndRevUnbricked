package me.mervyn.indrev_unbricked.blocks.machine

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.config.IRConfig
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext

class DirtOxygenatorBlock(registry: MachineRegistry, settings: FabricBlockSettings)
    : FacingMachineBlock(registry, settings, Tier.MK1, IRConfig.machines.dirtOxygenator, null) {

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return this.defaultState.with(FACING, ctx?.playerLookDirection)
    }
}