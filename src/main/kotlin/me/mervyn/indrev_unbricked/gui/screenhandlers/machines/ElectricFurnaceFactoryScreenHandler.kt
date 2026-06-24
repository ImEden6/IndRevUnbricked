package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.mervyn.indrev_unbricked.blockentities.crafters.ElectricFurnaceFactoryBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.ELECTRIC_FURNACE_FACTORY_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.upProcessBar
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class ElectricFurnaceFactoryScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        ELECTRIC_FURNACE_FACTORY_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.electric_furnace_factory", ctx, playerInventory, blockInventory, widgetPos = 0.15)
        withBlockEntity<ElectricFurnaceFactoryBlockEntity> { blockEntity ->
            val slotsAmount = 5
            val offset = 2.2

            for ((index, slot) in blockEntity.inventoryComponent!!.inventory.inputSlots.withIndex()) {
                val inputSlot = WItemSlot.of(blockInventory, slot)
                root.add(inputSlot, offset + (index * 1.4), 0.6)
            }

            for (i in 0 until slotsAmount) {
                val processWidget = upProcessBar(blockEntity, ElectricFurnaceFactoryBlockEntity.CRAFTING_COMPONENT_START_ID + i)
                root.add(processWidget, offset + (i * 1.4), 1.7)
            }

            for ((index, slot) in blockEntity.inventoryComponent!!.inventory.outputSlots.withIndex()) {
                val outputSlot = WItemSlot.of(blockInventory, slot)
                root.add(outputSlot, offset + (index * 1.4), 2.9)
                outputSlot.addChangeListener { _, _, _, _ ->
                    val player = playerInventory.player
                    if (!player.world.isClient) {
                        blockEntity.dropExperience(player)
                    }
                }
                outputSlot.isInsertingAllowed = false
            }
        }
        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("electric_furnace_factory_screen")
    }
}