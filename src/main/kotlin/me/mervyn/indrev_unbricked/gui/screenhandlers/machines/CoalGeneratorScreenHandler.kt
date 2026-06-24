package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItem
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.InputResult
import me.mervyn.indrev_unbricked.blockentities.generators.CoalGeneratorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.COAL_GENERATOR_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.WCustomBar
import me.mervyn.indrev_unbricked.gui.widgets.machines.fuelBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

class CoalGeneratorScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        COAL_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.coal_generator", ctx, playerInventory, blockInventory)

        val itemSlot = WItemSlot.of(blockInventory, 2)
        root.add(itemSlot, 4, 2)

        val wFuel = query<CoalGeneratorBlockEntity, WCustomBar> { be -> fuelBar(be) }
        root.add(wFuel, 4.1, 1.1)
        wFuel.setSize(14, 14)

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID: Identifier = identifier("coal_generator_screen")
    }
}