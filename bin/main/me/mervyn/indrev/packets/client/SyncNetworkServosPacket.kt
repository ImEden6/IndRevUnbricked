package me.mervyn.indrev.packets.client

import it.unimi.dsi.fastutil.objects.Object2ObjectFunction
import me.mervyn.indrev.IndustrialRevolutionClient
import me.mervyn.indrev.networks.Network
import me.mervyn.indrev.networks.client.ClientNetworkState
import me.mervyn.indrev.utils.identifier
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