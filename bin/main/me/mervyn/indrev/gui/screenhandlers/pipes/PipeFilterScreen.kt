package me.mervyn.indrev.gui.screenhandlers.pipes

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.entity.player.PlayerEntity
import me.mervyn.indrev.utils.literal

class PipeFilterScreen(val screenHandler: PipeFilterScreenHandler, val player: PlayerEntity) : CottonInventoryScreen<PipeFilterScreenHandler>(screenHandler, player, literal("Item Pipe Filter"))