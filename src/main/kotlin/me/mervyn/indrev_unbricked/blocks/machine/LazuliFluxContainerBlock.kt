package me.mervyn.indrev_unbricked.blocks.machine

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.api.sideconfigs.ConfigurationType
import me.mervyn.indrev_unbricked.blockentities.storage.LazuliFluxContainerBlockEntity
import me.mervyn.indrev_unbricked.config.IRConfig
import me.mervyn.indrev_unbricked.gui.screenhandlers.machines.LazuliFluxContainerScreenHandler
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class LazuliFluxContainerBlock(registry: MachineRegistry, settings: Settings, tier: Tier) : FacingMachineBlock(
    registry, settings, tier, when (tier) {
        Tier.MK1 -> IRConfig.machines.lazuliFluxContainerMk1
        Tier.MK2 -> IRConfig.machines.lazuliFluxContainerMk2
        Tier.MK3 -> IRConfig.machines.lazuliFluxContainerMk3
        else -> IRConfig.machines.lazuliFluxContainerMk4
    }, ::LazuliFluxContainerScreenHandler
) {

    override fun onPlaced(
        world: World?,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        if (world?.isClient == true) {
            val blockEntity = world.getBlockEntity(pos) as? LazuliFluxContainerBlockEntity ?: return
            ConfigurationType.values().forEach { type ->
                if (blockEntity.isConfigurable(type))
                    blockEntity.applyDefault(state, type, blockEntity.getCurrentConfiguration(type))
            }
        }
    }
}