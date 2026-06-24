package me.mervyn.indrev_unbricked.api.sideconfigs

import me.mervyn.indrev_unbricked.api.machines.TransferMode
import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction

interface Configurable {
    fun isConfigurable(type: ConfigurationType): Boolean
    fun isFixed(type: ConfigurationType): Boolean
    fun getValidConfigurations(type: ConfigurationType): Array<TransferMode>
    fun getCurrentConfiguration(type: ConfigurationType): SideConfiguration
    fun applyDefault(state: BlockState, type: ConfigurationType, configuration: MutableMap<Direction, TransferMode>)

}