package me.mervyn.indrev.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WSlider
import io.github.cottonmc.cotton.gui.widget.WSprite
import io.github.cottonmc.cotton.gui.widget.data.Axis
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.mervyn.indrev.blockentities.farms.AOEMachineBlockEntity
import me.mervyn.indrev.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev.gui.screenhandlers.SLAUGHTER_HANDLER
import me.mervyn.indrev.gui.widgets.misc.WText
import me.mervyn.indrev.gui.widgets.misc.WTooltipedItemSlot
import me.mervyn.indrev.inventories.IRInventory
import me.mervyn.indrev.utils.add
import me.mervyn.indrev.utils.configure
import me.mervyn.indrev.utils.identifier
import me.mervyn.indrev.utils.setIcon
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev.utils.translatable

class SlaughterScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        SLAUGHTER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    private var value = -1

    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.slaughter", ctx, playerInventory, blockInventory)

        val outputFrame = WSprite(identifier("textures/gui/output_frame.png"))
        root.add(outputFrame, 5.1, 0.7)
        outputFrame.setSize(58, 62)

        val outputSlot = WTooltipedItemSlot.of(
            blockInventory,
            (blockInventory as IRInventory).outputSlots.first(),
            3,
            3,
            translatable("gui.indrev.output_slot_type")
        )
        outputSlot.isInsertingAllowed = false
        root.add(outputSlot, 5.2, 1.0)

        val swordSlot = WTooltipedItemSlot.of(blockInventory, 1, translatable("gui.indrev.slaughter_input_sword"))
        swordSlot.setIcon(ctx, blockInventory, 1, SWORD_ICON)
        root.add(swordSlot, 2.5, 1.5)

        val slider = WSlider(1, 10, Axis.HORIZONTAL)
        root.add(slider, 1.6, 3.1)
        slider.setSize(50, 20)
        ctx.run { world, pos ->
            val blockEntity = world.getBlockEntity(pos) as? AOEMachineBlockEntity<*> ?: return@run
            slider.value = blockEntity.range
        }
        slider.setValueChangeListener { newValue -> this.value = newValue }

        val text = WText({
            translatable("block.indrev.aoe.range", slider.value)
        }, HorizontalAlignment.LEFT)
        root.add(text, 1.8, 2.8)

        root.validate(this)
    }

    override fun onClosed(player: PlayerEntity?) {
        super.onClosed(player)
        AOEMachineBlockEntity.sendValueUpdatePacket(value, ctx)
    }

    companion object {
        val SCREEN_ID = identifier("slaughter_screen")
        val SWORD_ICON = identifier("textures/gui/sword_icon.png")
    }
}