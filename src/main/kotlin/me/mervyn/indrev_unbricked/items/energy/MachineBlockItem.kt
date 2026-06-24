package me.mervyn.indrev_unbricked.items.energy

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.blocks.machine.MachineBlock
import me.mervyn.indrev_unbricked.gui.tooltip.energy.EnergyTooltipData
import me.mervyn.indrev_unbricked.utils.buildMachineTooltip
import me.mervyn.indrev_unbricked.utils.energyOf
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.impl.SimpleItemEnergyStorageImpl
import java.util.*

class MachineBlockItem(private val machineBlock: Block, settings: Settings) : BlockItem(machineBlock, settings) {

    init {
        val capacity = ((machineBlock as? MachineBlock)?.config?.maxEnergyStored ?: 0).toLong()
        EnergyStorage.ITEM.registerForItems({ _, ctx -> SimpleItemEnergyStorageImpl.createSimpleStorage(ctx, capacity, Tier.MK4.io, 0) }, this)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        options: TooltipContext?
    ) {
        val config = (machineBlock as? MachineBlock)?.config
        buildMachineTooltip(config ?: return, tooltip)
    }

    override fun getTooltipData(stack: ItemStack): Optional<TooltipData> {
        val handler = energyOf(stack) ?: return Optional.empty()
        return Optional.of(EnergyTooltipData(handler.amount, handler.capacity))
    }
}