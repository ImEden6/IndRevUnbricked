package me.mervyn.indrev_unbricked.packets.common

import me.mervyn.indrev_unbricked.blockentities.crafters.CraftingMachineBlockEntity
import me.mervyn.indrev_unbricked.utils.identifier
import me.mervyn.indrev_unbricked.utils.isLoaded
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ToggleFactoryStackSplittingPacket  {

    val SPLIT_STACKS_PACKET = identifier("split_stacks_packet") 

     fun register() {
        ServerPlayNetworking.registerGlobalReceiver(SPLIT_STACKS_PACKET) { server, player, _, buf, _ ->
            val pos = buf.readBlockPos()
            server.execute {
                val world = player.world
                if (world.isLoaded(pos)) {
                    val blockEntity = world.getBlockEntity(pos) as? CraftingMachineBlockEntity<*> ?: return@execute
                    blockEntity.isSplitOn = !blockEntity.isSplitOn
                    if (blockEntity.isSplitOn) blockEntity.splitStacks()
                }
            }
        }

    }
}