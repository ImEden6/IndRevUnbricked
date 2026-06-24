package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.mervyn.indrev_unbricked.blockentities.miningrig.DrillBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.DRILL_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.misc.WText
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev_unbricked.utils.translatable
import java.util.function.Predicate

class MiningRigDrillScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        DRILL_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {

    init {
        val root = WGridPanel()
        setRootPanel(root)

        root.add(WText(translatable("block.indrev_unbricked.drill"), HorizontalAlignment.LEFT, 0x404040), 0.0, -0.1)

        val slot = WItemSlot.of(blockInventory, 0)
        slot.filter = Predicate { stack -> DrillBlockEntity.isValidDrill(stack.item) }
        root.add(slot, 4, 2)

        root.add(createPlayerInventoryPanel(), 0.0, 3.8)

        root.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("drill")
    }
}