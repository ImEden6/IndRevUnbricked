package me.mervyn.indrev.packets.common

import me.mervyn.indrev.blockentities.miningrig.DataCardWriterBlockEntity
import me.mervyn.indrev.utils.identifier
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object DataCardWriteStartPacket {

    val START_PACKET = identifier("write_data_card_start")

    fun register() {
        ServerPlayNetworking.registerGlobalReceiver(START_PACKET) { server, player, _, buf, _ ->
            val pos = buf.readBlockPos()
            server.execute {
                val blockEntity = player.world.getBlockEntity(pos) as? DataCardWriterBlockEntity ?: return@execute
                blockEntity.start()
            }
        }
    }
}