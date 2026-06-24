package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.gui.screenhandlers.FISHER_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.misc.WTooltipedItemSlot
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import me.mervyn.indrev_unbricked.utils.setIcon
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev_unbricked.utils.translatable

class FisherScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        FISHER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.fisher", ctx, playerInventory, blockInventory)

        root.add(WItemSlot.of(blockInventory, 2, 2, 2), 3.7, 0.7)

        val fishingRodSlot = WTooltipedItemSlot.of(blockInventory, 1, translatable("gui.indrev.fishingrod"))
        fishingRodSlot.setIcon(ctx, blockInventory, 1, FISHING_ROD_ICON)
        root.add(fishingRodSlot, 4.2, 3.0)

        root.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("fishing_farm_screen")
        val FISHING_ROD_ICON = identifier("textures/gui/fishing_rod_icon.png")
    }
}