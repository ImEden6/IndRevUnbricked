package me.mervyn.indrev.networks.energy

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blocks.machine.pipes.CableBlock
import me.mervyn.indrev.config.IRConfig
import me.mervyn.indrev.networks.Network
import me.mervyn.indrev.utils.energyOf
import me.mervyn.indrev.utils.insert
import me.mervyn.indrev.utils.isLoaded
import me.mervyn.indrev.utils.transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Block
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import team.reborn.energy.api.EnergyStorage
import java.util.*

open class EnergyNetwork(
    world: ServerWorld,
    val cables: MutableSet<BlockPos> = ObjectOpenHashSet(),
    val machines: MutableMap<BlockPos, EnumSet<Direction>> = Object2ObjectOpenHashMap()
) : Network(Type.ENERGY, world, cables, machines) {

    var tier = Tier.MK1

    val maxCableTransfer: Long
        get() = when (tier) {
            Tier.MK1 -> IRConfig.cables.cableMk1
            Tier.MK2 -> IRConfig.cables.cableMk2
            Tier.MK3 -> IRConfig.cables.cableMk3
            else -> IRConfig.cables.cableMk4
        }.toLong()

    val insertables = ObjectOpenHashSet<BlockPos>()

    var energy = 0L
    val capacity: Long get() = pipes.size * maxCableTransfer


    private val maxInputs = Object2LongOpenHashMap<EnergyStorage>()
    private val storages = mutableListOf<EnergyStorage>()

    override fun tick(world: ServerWorld) {
        if (energy <= 0) return

        maxInputs.clear()
        storages.clear()

        insertables.forEach { pos ->
            if (world.isLoaded(pos)) {
                machines[pos]?.forEach { dir ->
                    val storage = energyOf(world, pos, dir)
                    if (storage != null)
                        storages.add(storage)
                }
            }
        }

        val totalInput = transaction { tx ->
            storages.sumOf { energyStorage ->
                val maxInput = energyStorage.insert(MAX_VALUE, tx)
                if (maxInput > 0)
                    maxInputs[energyStorage] = maxInput
                maxInput
            }.toDouble()
        }

        if (totalInput <= 0) return

        transaction { tx ->
            storages.forEach { energyStorage ->
                val maxInput = maxInputs.getLong(energyStorage)
                if (maxInput <= 0) return@forEach

                val toTransfer = ((maxInput / totalInput) * energy).toLong().coerceAtMost(maxCableTransfer).coerceAtMost(energy)

                energy -= energyStorage.insert(toTransfer, tx)

                if (energy <= 0) {
                    tx.commit()
                    return
                }
            }

            tx.commit()
        }
    }

    override fun appendPipe(block: Block, blockPos: BlockPos) {
        val cable = block as? CableBlock ?: return
        this.tier = cable.tier
        super.appendPipe(block, blockPos)
    }

    companion object {
        private const val MAX_VALUE = Long.MAX_VALUE - 1L
    }
}