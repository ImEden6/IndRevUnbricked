package me.mervyn.indrev.networks.fluid

import me.mervyn.indrev.networks.Network
import me.mervyn.indrev.networks.ServoNetworkState
import net.minecraft.server.world.ServerWorld

class FluidNetworkState(world: ServerWorld) : ServoNetworkState<FluidNetwork>(Network.Type.FLUID, world)