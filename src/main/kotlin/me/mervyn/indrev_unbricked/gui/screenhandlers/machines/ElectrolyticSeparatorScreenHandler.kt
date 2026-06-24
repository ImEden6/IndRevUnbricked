package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import me.mervyn.indrev_unbricked.blockentities.crafters.ElectrolyticSeparatorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.ELECTROLYTIC_SEPARATOR_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.fluidTank
import me.mervyn.indrev_unbricked.gui.widgets.machines.leftProcessBar
import me.mervyn.indrev_unbricked.gui.widgets.machines.processBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class ElectrolyticSeparatorScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        ELECTROLYTIC_SEPARATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev_unbricked.electrolytic_separator", ctx, playerInventory, blockInventory)

        withBlockEntity<ElectrolyticSeparatorBlockEntity> { be ->
            val fluid = fluidTank(be, ElectrolyticSeparatorBlockEntity.INPUT_TANK_ID)
            root.add(fluid, 5.0, 1.0)

            val processWidget = processBar(be, ElectrolyticSeparatorBlockEntity.CRAFTING_COMPONENT_ID)
            root.add(processWidget, 6.2, 1.8)

            val leftProcessWidget = leftProcessBar(be, ElectrolyticSeparatorBlockEntity.CRAFTING_COMPONENT_ID)
            root.add(leftProcessWidget, 3.7, 1.8)

            val firstOutputFluid = fluidTank(be, ElectrolyticSeparatorBlockEntity.FIRST_OUTPUT_TANK_ID)
            root.add(firstOutputFluid, 2.5, 1.0)

            val secondOutputFluid = fluidTank(be, ElectrolyticSeparatorBlockEntity.SECOND_OUTPUT_TANK_ID)
            root.add(secondOutputFluid, 7.5, 1.0)
        }

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("electrolytic_separator")
    }
}