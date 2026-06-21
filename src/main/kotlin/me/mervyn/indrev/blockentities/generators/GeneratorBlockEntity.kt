package me.mervyn.indrev.blockentities.generators

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blockentities.MachineBlockEntity
import me.mervyn.indrev.config.GeneratorConfig
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

abstract class GeneratorBlockEntity(tier: Tier, registry: MachineRegistry, pos: BlockPos, state: BlockState) :
    MachineBlockEntity<GeneratorConfig>(tier, registry, pos, state) {

    override fun machineTick() {
        if (world?.isClient == false) {
            val ratio = getGenerationRatio()
            if (shouldGenerate()) {
                this.energy += ratio.coerceAtMost(energyCapacity - energy)
                this.temperatureComponent?.tick(true)
                workingState = true
            } else {
                workingState = false
                this.temperatureComponent?.tick(false)
            }
        }
    }

    override val maxInput: Long = 0
    override val maxOutput: Long = config.maxOutput

    abstract fun shouldGenerate(): Boolean

    open fun getGenerationRatio(): Long = (config.ratio * if (this.temperatureComponent?.isFullEfficiency() == true) config.temperatureBoost else 1.0).toLong()
}