package me.mervyn.indrev_unbricked.blocks.machine.solarpowerplant

import me.mervyn.indrev_unbricked.blockentities.solarpowerplant.SolarReceiverBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class SolarReceiverBlock(settings: Settings) : Block(settings), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = SolarReceiverBlockEntity(pos, state)
}