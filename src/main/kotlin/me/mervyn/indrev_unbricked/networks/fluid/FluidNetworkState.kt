package me.mervyn.indrev_unbricked.networks.fluid

import me.mervyn.indrev_unbricked.networks.Network
import me.mervyn.indrev_unbricked.networks.ServoNetworkState
import net.minecraft.server.world.ServerWorld

class FluidNetworkState(world: ServerWorld) : ServoNetworkState<FluidNetwork>(Network.Type.FLUID, world)