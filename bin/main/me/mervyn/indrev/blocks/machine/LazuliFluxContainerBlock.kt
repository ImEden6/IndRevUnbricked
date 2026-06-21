package me.mervyn.indrev.blocks.machine

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.api.sideconfigs.ConfigurationType
import me.mervyn.indrev.blockentities.storage.LazuliFluxContainerBlockEntity
import me.mervyn.indrev.config.IRConfig
import me.mervyn.indrev.gui.screenhandlers.machines.LazuliFluxContainerScreenHandler
import me.mervyn.indrev.registry.MachineRegistry
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