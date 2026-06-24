package me.mervyn.indrev_unbricked.api

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import me.mervyn.indrev_unbricked.networks.energy.EnergyNetworkState
import me.mervyn.indrev_unbricked.networks.fluid.FluidNetworkState
import me.mervyn.indrev_unbricked.networks.item.ItemNetworkState
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache
import net.minecraft.util.math.Direction
import team.reborn.energy.api.EnergyStorage

interface ServerWorldExtension {
    fun indrev_getEnergyCache(): Long2ObjectOpenHashMap<BlockApiCache<EnergyStorage, Direction?>>

    fun indrev_getEnergyNetworkState(): EnergyNetworkState

    fun indrev_getFluidNetworkState(): FluidNetworkState

    fun indrev_getItemNetworkState(): ItemNetworkState
}