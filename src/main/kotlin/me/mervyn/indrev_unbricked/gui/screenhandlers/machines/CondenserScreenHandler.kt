package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.crafters.CondenserBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.CONDENSER_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.fluidTank
import me.mervyn.indrev_unbricked.gui.widgets.machines.processBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class CondenserScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        CONDENSER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.condenser", ctx, playerInventory, blockInventory)

        withBlockEntity<CondenserBlockEntity> { be ->
            val fluid = fluidTank(be, CondenserBlockEntity.INPUT_TANK_ID)
            root.add(fluid, 2.8, 1.0)

            val processWidget = processBar(be, CondenserBlockEntity.CRAFTING_COMPONENT_ID)
            root.add(processWidget, 4.0, 1.8)
        }

        val outputSlot = WItemSlot.outputOf(blockInventory, 2)
        root.add(outputSlot, 5.7, 1.8)

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("condenser")
    }
}