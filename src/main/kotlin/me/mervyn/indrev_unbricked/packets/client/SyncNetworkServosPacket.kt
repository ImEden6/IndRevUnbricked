package me.mervyn.indrev_unbricked.packets.client

import it.unimi.dsi.fastutil.objects.Object2ObjectFunction
import me.mervyn.indrev_unbricked.IndustrialRevolutionClient
import me.mervyn.indrev_unbricked.networks.Network
import me.mervyn.indrev_unbricked.networks.client.ClientNetworkState
import me.mervyn.indrev_unbricked.utils.identifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object SyncNetworkServosPacket {

    val SYNC_NETWORK_SERVOS = identifier("sync_network_servos") 

     fun register() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_NETWORK_SERVOS) { client, _, buf, _ ->
            val type = Network.Type.valueOf(buf.readString())
            val state = IndustrialRevolutionClient.CLIENT_NETWORK_STATE.computeIfAbsent(type, Object2ObjectFunction { ClientNetworkState(type) })
            state.processPacket(buf, client)
        }
    }
}