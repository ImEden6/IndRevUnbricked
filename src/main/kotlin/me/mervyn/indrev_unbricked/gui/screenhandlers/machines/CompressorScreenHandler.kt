package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.crafters.CompressorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.COMPRESSOR_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.WCustomBar
import me.mervyn.indrev_unbricked.gui.widgets.machines.processBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class CompressorScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        COMPRESSOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev_unbricked.compressor", ctx, playerInventory, blockInventory)

        val inputSlot = WItemSlot.of(blockInventory, 2)
        root.add(inputSlot, 3.3, 1.8)

        val processWidget = query<CompressorBlockEntity, WCustomBar> { be -> processBar(be, CompressorBlockEntity.CRAFTING_COMPONENT_ID) }
        root.add(processWidget, 4.45, 1.8)

        val outputSlot = WItemSlot.outputOf(blockInventory, 3)
        outputSlot.isInsertingAllowed = false
        root.add(outputSlot, 5.94, 1.8)

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("compressor_screen")
    }
}