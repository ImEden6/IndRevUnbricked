package me.mervyn.indrev_unbricked.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev_unbricked.api.machines.Tier
import me.mervyn.indrev_unbricked.blocks.models.pipes.CableModel

class DashCableModel : DashObject<CableModel> {
    val tier: Int
    val models: IntArray
    val sprites: IntArray

    constructor(
        tier: Int,
        models: IntArray,
        sprites: IntArray
    ) {
        this.tier = tier
        this.models = models
        this.sprites = sprites
    }

    constructor(model: CableModel, writer: RegistryWriter) {
        this.tier = model.tier.ordinal
        this.models = model.modelArray.map { m -> if (m != null) writer.add(m) else -1 }.toIntArray()
        this.sprites = model.spriteArray.map { s -> if (s != null) writer.add(s) else -1 }.toIntArray()
    }

    override fun export(reader: RegistryReader): CableModel {
        val model = CableModel(Tier.ALL_VALUES[tier])
        this.models.forEachIndexed { index, pointer ->
            if (pointer != -1) {
                model.modelArray[index] = reader.get(pointer)
            }
        }
        this.sprites.forEachIndexed { index, pointer ->
            if (pointer != -1) {
                model.spriteArray[index] = reader.get(pointer)
            }
        }
        model.transform = model.modelArray[0]!!.transformation
        model.buildMeshes()
        return model
    }
}