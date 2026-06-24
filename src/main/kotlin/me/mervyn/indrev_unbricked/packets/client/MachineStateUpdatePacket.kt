package me.mervyn.indrev_unbricked.packets.client

import me.mervyn.indrev_unbricked.blockentities.GlobalStateController
import me.mervyn.indrev_unbricked.utils.identifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object MachineStateUpdatePacket {

    val UPDATE_PACKET_ID = identifier("global_state_update") 

     fun register() {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_PACKET_ID) { client, _, buf, _ ->
            val pos = buf.readBlockPos()
            val workingState = buf.readBoolean()
            client.execute {
                GlobalStateController.workingStateTracker[pos.asLong()] = workingState
                GlobalStateController.queueUpdate(pos)
            }
        }
    }
}