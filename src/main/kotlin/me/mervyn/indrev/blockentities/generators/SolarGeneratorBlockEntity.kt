package me.mervyn.indrev.blockentities.generators

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.components.TemperatureComponent
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class SolarGeneratorBlockEntity(tier: Tier, pos: BlockPos, state: BlockState) :
    GeneratorBlockEntity(tier, MachineRegistry.SOLAR_GENERATOR_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.1, 500..700, 1000)
        this.inventoryComponent = inventory(this) {}

    }

    override fun shouldGenerate(): Boolean = this.world?.isSkyVisible(pos.up()) == true && this.world?.isDay == true && energy < energyCapacity
}