package me.mervyn.indrev.gui.screenhandlers.storage

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.mervyn.indrev.gui.screenhandlers.CABINET_HANDLER
import me.mervyn.indrev.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev.gui.widgets.misc.WText
import me.mervyn.indrev.utils.add
import me.mervyn.indrev.utils.identifier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev.utils.translatable

class CabinetScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        CABINET_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)

        root.add(WText(translatable("block.indrev.cabinet"), HorizontalAlignment.LEFT, 0x404040), 0.0, -0.1)

        root.add(WItemSlot.of(blockInventory, 0, 9, 3), 0.0, 0.6)

        root.add(createPlayerInventoryPanel(), 0.0, 3.8)

        root.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("cabinet")
    }
}