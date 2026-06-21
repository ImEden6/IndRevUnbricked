package me.mervyn.indrev.packets.common

import me.mervyn.indrev.blockentities.farms.AOEMachineBlockEntity
import me.mervyn.indrev.utils.identifier
import me.mervyn.indrev.utils.isLoaded
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object UpdateAOEMachineRangePacket  {

    val UPDATE_VALUE_PACKET_ID = identifier("update_value_packet") 

     fun register() {
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_VALUE_PACKET_ID) { server, player, _, buf, _ ->
            val value = buf.readInt()
            val pos = buf.readBlockPos()
            val world = player.world
            server.execute {
                if (world.isLoaded(pos)) {
                    val blockEntity = world.getBlockEntity(pos) as? AOEMachineBlockEntity<*> ?: return@execute
                    blockEntity.range = value
                    blockEntity.markDirty()
                    blockEntity.sync()
                }
            }
        }
    }
}