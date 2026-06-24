package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.generators.BiomassGeneratorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.BIOMASS_GENERATOR_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.WCustomBar
import me.mervyn.indrev_unbricked.gui.widgets.machines.fuelBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class BiomassGeneratorScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        BIOMASS_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.biomass_generator", ctx, playerInventory, blockInventory)

        // Fuel input
        root.add(WItemSlot.of(blockInventory, 2), 4, 2)
        // Burning widget

        val wFuel = query<BiomassGeneratorBlockEntity, WCustomBar> { be -> fuelBar(be) }
        root.add(wFuel, 4.1, 1.1)
        wFuel.setSize(14, 14)

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("biomass_generator")
    }
}