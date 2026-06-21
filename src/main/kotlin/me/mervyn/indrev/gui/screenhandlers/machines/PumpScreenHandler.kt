package me.mervyn.indrev.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WDynamicLabel
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import me.mervyn.indrev.blockentities.crafters.PulverizerBlockEntity
import me.mervyn.indrev.blockentities.farms.PumpBlockEntity
import me.mervyn.indrev.blockentities.generators.GasBurningGeneratorBlockEntity
import me.mervyn.indrev.blocks.machine.PumpBlock
import me.mervyn.indrev.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev.gui.screenhandlers.PULVERIZER_HANDLER
import me.mervyn.indrev.gui.screenhandlers.PUMP_HANDLER
import me.mervyn.indrev.gui.widgets.machines.WCustomBar
import me.mervyn.indrev.gui.widgets.machines.fluidTank
import me.mervyn.indrev.gui.widgets.machines.processBar
import me.mervyn.indrev.items.upgrade.Enhancer
import me.mervyn.indrev.utils.add
import me.mervyn.indrev.utils.configure
import me.mervyn.indrev.utils.identifier
import me.mervyn.indrev.utils.literal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class PumpScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        PUMP_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.pump", ctx, playerInventory, blockInventory)
        withBlockEntity<PumpBlockEntity> { be ->
            val fluid = fluidTank(be, PumpBlockEntity.TANK_ID)
            root.add(fluid, 8, 1)
        }

        root.add(WLabel(literal("1 bucket every")), 1, 1)
        root.add(WDynamicLabel { "${component!!.get<Int>(PumpBlockEntity.SPEED_ID)} ticks" }, 1, 2)


        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("pump_screen")
    }
}