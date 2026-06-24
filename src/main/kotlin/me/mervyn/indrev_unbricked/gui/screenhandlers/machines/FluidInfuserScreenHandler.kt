package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.crafters.FluidInfuserBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.FLUID_INFUSER_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.fluidTank
import me.mervyn.indrev_unbricked.gui.widgets.machines.processBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class FluidInfuserScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        FLUID_INFUSER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.fluid_infuser", ctx, playerInventory, blockInventory)

        val firstInput = WItemSlot.of(blockInventory, 2)
        root.add(firstInput, 3.7, 1.8)

        withBlockEntity<FluidInfuserBlockEntity> { be ->
            val fluid = fluidTank(be, FluidInfuserBlockEntity.INPUT_TANK_ID)
            root.add(fluid, 2.5, 1.0)

            val processWidget = processBar(be, FluidInfuserBlockEntity.CRAFTING_COMPONENT_ID)
            root.add(processWidget, 5.0, 1.8)

            val outputStack = WItemSlot.of(blockInventory, 3)
            root.add(outputStack, 6.4, 1.8)

            val outputFluid = fluidTank(be, FluidInfuserBlockEntity.OUTPUT_TANK_ID)
            root.add(outputFluid, 7.7, 1.0)
        }

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("fluid_infuser")
    }
}