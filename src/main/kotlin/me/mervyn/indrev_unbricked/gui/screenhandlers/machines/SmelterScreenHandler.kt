package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.crafters.SmelterBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.screenhandlers.SMELTER_HANDLER
import me.mervyn.indrev_unbricked.gui.widgets.machines.fluidTank
import me.mervyn.indrev_unbricked.gui.widgets.machines.processBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class SmelterScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        SMELTER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev_unbricked.smelter", ctx, playerInventory, blockInventory)

        val inputSlot = WItemSlot.of(blockInventory, 2)
        root.add(inputSlot, 3.5, 1.8)

        withBlockEntity<SmelterBlockEntity> { be ->
            val processWidget = processBar(be, SmelterBlockEntity.CRAFTING_COMPONENT_ID)
            root.add(processWidget, 4.8, 1.8)

            val fluid = fluidTank(be, SmelterBlockEntity.TANK_ID)
            root.add(fluid, 6.2, 1.0)
        }

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("smelter")
    }
}