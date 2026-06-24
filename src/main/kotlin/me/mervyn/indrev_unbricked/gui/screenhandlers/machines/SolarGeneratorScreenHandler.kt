package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.mervyn.indrev_unbricked.blockentities.generators.SolarGeneratorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.screenhandlers.SOLAR_GENERATOR_HANDLER
import me.mervyn.indrev_unbricked.gui.widgets.misc.WDynamicSprite
import me.mervyn.indrev_unbricked.gui.widgets.misc.WText
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev_unbricked.utils.translatable

class SolarGeneratorScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        SOLAR_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev_unbricked.solar_generator", ctx, playerInventory, blockInventory)

        ctx.run { world, pos ->
            val blockEntity = world.getBlockEntity(pos) as? SolarGeneratorBlockEntity ?: return@run
            val sprite = WDynamicSprite { if (blockEntity.shouldGenerate()) GENERATING_ICON else NOT_GENERATING_ICON }
            root.add(sprite, 4.5, 2.0)
            sprite.setSize(19, 10)

            val text = WText({
                if (blockEntity.shouldGenerate()) translatable("gui.indrev_unbricked.solar.on")
                else translatable("gui.indrev_unbricked.heatgen.idle")
            }, HorizontalAlignment.CENTER, 0x404040)
            root.add(text, 5.0, 2.7)
        }

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("solar_generator")
        private val GENERATING_ICON = identifier("textures/gui/sun_icon.png")
        private val NOT_GENERATING_ICON = identifier("textures/gui/sun_unlit_icon.png")
    }
}