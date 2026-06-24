package me.mervyn.indrev_unbricked.blockentities.generators

import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.components.TemperatureComponent
import me.mervyn.indrev_unbricked.inventories.inventory
import me.mervyn.indrev_unbricked.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos

class CoalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    SolidFuelGeneratorBlockEntity(Tier.MK1, MachineRegistry.COAL_GENERATOR_REGISTRY, pos, state) {

    init {
        this.temperatureComponent = TemperatureComponent(this, 0.08, 900..2000, 2500)
        this.inventoryComponent = inventory(this) {
            input {
                slot = 2
                2 filter { stack -> BURN_TIME_MAP.containsKey(stack.item) }
            }
        }
    }

    override fun getFuelMap(): Map<Item, Int> = BURN_TIME_MAP

    companion object {
        private val BURN_TIME_MAP = AbstractFurnaceBlockEntity.createFuelTimeMap()
    }
}