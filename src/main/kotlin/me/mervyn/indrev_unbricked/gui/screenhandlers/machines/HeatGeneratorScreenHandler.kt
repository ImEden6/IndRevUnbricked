package me.mervyn.indrev_unbricked.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.mervyn.indrev_unbricked.blockentities.generators.HeatGeneratorBlockEntity
import me.mervyn.indrev_unbricked.gui.screenhandlers.HEAT_GENERATOR_HANDLER
import me.mervyn.indrev_unbricked.gui.screenhandlers.IRGuiScreenHandler
import me.mervyn.indrev_unbricked.gui.widgets.machines.fluidTank
import me.mervyn.indrev_unbricked.gui.widgets.misc.WStaticTooltip
import me.mervyn.indrev_unbricked.gui.widgets.misc.WText
import me.mervyn.indrev_unbricked.utils.add
import me.mervyn.indrev_unbricked.utils.configure
import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import me.mervyn.indrev_unbricked.utils.literal
import me.mervyn.indrev_unbricked.utils.translatable
import net.minecraft.util.Formatting

class HeatGeneratorScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiScreenHandler(
        HEAT_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.heat_generator", ctx, playerInventory, blockInventory, invPos = 4.25)

        val info = WStaticTooltip()
        root.add(info, 2.3, 0.9)
        info.setSize(90, 55)

        ctx.run { world, pos ->
            val blockEntity = world.getBlockEntity(pos) as? HeatGeneratorBlockEntity ?: return@run
            val generatingText = WText({
                val consumptionRate = (blockEntity.getConsumptionRate(component!![HeatGeneratorBlockEntity.CONSUMPTION_RATIO_ID])).toString()
                translatable("gui.indrev.heatgen.title", literal(consumptionRate).formatted(Formatting.DARK_RED)).formatted(Formatting.RED)
            }, HorizontalAlignment.LEFT)
            root.add(generatingText, 2.5, 1.0)
        }
        root.add(WText(translatable("gui.indrev.heatgen.pertick").formatted(Formatting.RED), HorizontalAlignment.LEFT), 2.5, 1.6)

        val amount = WText({
            val ratio = component!!.get<Long>(HeatGeneratorBlockEntity.GENERATION_RATIO_ID)
            translatable("gui.indrev.heatgen.generating", literal(ratio.toString()).formatted(Formatting.WHITE)).formatted(Formatting.BLUE)
        }, HorizontalAlignment.LEFT)
        root.add(amount, 2.5, 2.6)

        root.add(WText(translatable("gui.indrev.heatgen.pertick").formatted(Formatting.BLUE), HorizontalAlignment.LEFT), 2.5, 3.2)

        withBlockEntity<HeatGeneratorBlockEntity> { be ->
            val fluid = fluidTank(be, HeatGeneratorBlockEntity.TANK_ID)
            root.add(fluid, 8.0, 0.6)
        }

        root.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("heat_generator_screen")
    }
}