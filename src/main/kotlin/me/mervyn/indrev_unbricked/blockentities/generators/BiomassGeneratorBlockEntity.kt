package me.mervyn.indrev_unbricked.blockentities.generators

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.TemperatureComponent
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.registry.IRItemRegistry
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos

class BiomassGeneratorBlockEntity(tier: Tier, pos: BlockPos, state: BlockState)
    : SolidFuelGeneratorBlockEntity(tier, MachineRegistry.BIOMASS_GENERATOR_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.08, 900..2000, 2500)
        this.inventoryComponent = inventory(this) {
            input { slot = 2 }
        }
    }

    override fun getFuelMap(): Map<Item, Int> = BURN_TIME_MAP

    companion object {
        private val BURN_TIME_MAP = hashMapOf<Item, Int>()

        init {
            BURN_TIME_MAP[IRItemRegistry.BIOMASS] = 150
        }
    }
}