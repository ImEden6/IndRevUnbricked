package me.mervyn.indrev_unbricked.items.misc

import me.mervyn.indrev_unbricked.blockentities.MachineBlockEntity
import me.mervyn.indrev_unbricked.blockentities.storage.LazuliFluxContainerBlockEntity
import me.mervyn.indrev_unbricked.config.BasicMachineConfig
import me.mervyn.indrev_unbricked.utils.energyOf
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import me.mervyn.indrev_unbricked.utils.literal
import me.mervyn.indrev_unbricked.utils.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting

class IREnergyReaderItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        if (context?.world?.isClient == true) return ActionResult.SUCCESS
        val blockPos = context?.blockPos
        val blockEntity = context?.world?.getBlockEntity(blockPos)
        val machineIo = energyOf(context!!.world as ServerWorld, blockPos!!, context.side)
        if (machineIo != null) {
            val energy = machineIo.amount
            val text = translatable("item.indrev_unbricked.energy_reader.use")
                .formatted(Formatting.BLUE)
                .append(literal(" $energy LF").formatted(Formatting.WHITE))
            if (blockEntity is MachineBlockEntity<*>) {
                val energyCost =
                    when {
                        blockEntity !is LazuliFluxContainerBlockEntity && blockEntity.config is BasicMachineConfig ->
                            blockEntity.getEnergyCost()
                        else -> -1
                    }
                if (energyCost > 0) {
                    val energyCostText = translatable(
                        "item.indrev.energy_reader.use1",
                        literal(energyCost.toString()).formatted(Formatting.WHITE)
                    ).formatted(Formatting.BLUE)
                    text
                        .append(literal(" | ").formatted(Formatting.BLACK, Formatting.BOLD))
                        .append(energyCostText)
                }
            }
            context.player?.sendMessage(text, true)
        }
        return ActionResult.FAIL
    }
}