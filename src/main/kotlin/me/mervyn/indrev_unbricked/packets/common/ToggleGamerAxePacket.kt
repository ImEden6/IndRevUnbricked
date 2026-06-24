package me.mervyn.indrev_unbricked.packets.common

import me.mervyn.indrev_unbricked.registry.IRItemRegistry
import me.mervyn.indrev_unbricked.utils.energyOf
import me.mervyn.indrev_unbricked.utils.identifier
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import me.mervyn.indrev_unbricked.utils.literal
import me.mervyn.indrev_unbricked.utils.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.TypedActionResult

object ToggleGamerAxePacket {
    val PACKET_ID = identifier("toggle_gamer_axe")

    fun register() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID) { server, player, _, _, _ ->
            server.execute {
                val stack = player.mainHandStack
                if (stack.isOf(IRItemRegistry.GAMER_AXE_ITEM)) {
                    val tag = stack?.orCreateNbt
                    if (tag?.contains("Active") == false || tag?.contains("Progress") == false) {
                        tag.putBoolean("Active", true)
                        tag.putFloat("Progress", 0f)
                    } else if (tag?.contains("Active") == true) {
                        val active = !tag.getBoolean("Active")
                        val itemIo = energyOf(stack)!!
                        if (itemIo.amount > 0) {
                            stack.orCreateNbt.putBoolean("Active", active)
                            val color = if (active) Formatting.GREEN else Formatting.RED
                            player.sendMessage(literal("").append(stack.name).formatted(stack.rarity.formatting).append(": ").append(translatable("item.indrev_unbricked.gamer_axe.$active").formatted(color, Formatting.BOLD)), true)
                        } else {
                            player.sendMessage(literal("Not enough energy!"), true)
                        }
                    }
                }
            }
        }
    }
}