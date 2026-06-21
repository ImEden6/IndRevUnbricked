package me.mervyn.indrev.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blocks.models.pipes.ItemPipeModel

class DashItemPipeModel : DashObject<ItemPipeModel> {
    val tier: Int
    val models: IntArray
    val sprites: IntArray
    val retrieverServos: IntArray
    val outputServos: IntArray

    constructor(
        tier: Int,
        models: IntArray,
        sprites: IntArray,
        retrieverServos: IntArray,
        outputServos: IntArray
    ) {
        this.tier = tier
        this.models = models
        this.sprites = sprites
        this.retrieverServos = retrieverServos
        this.outputServos = outputServos
    }

    constructor(model: ItemPipeModel, writer: RegistryWriter) {
        this.tier = model.tier.ordinal
        this.models = model.modelArray.map { m -> if (m != null) writer.add(m) else -1 }.toIntArray()
        this.sprites = model.spriteArray.map { s -> if (s != null) writer.add(s) else -1 }.toIntArray()
        this.retrieverServos = model.retrieverServoModels.map { m -> if (m != null) writer.add(m) else -1 }.toIntArray()
        this.outputServos = model.outputServoModels.map { m -> if (m != null) writer.add(m) else -1 }.toIntArray()
    }

    override fun export(reader: RegistryReader): ItemPipeModel {
        val model = ItemPipeModel(Tier.ALL_VALUES[tier])
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
        this.retrieverServos.forEachIndexed { index, pointer ->
            if (pointer != -1) {
                model.retrieverServoModels[index] = reader.get(pointer)
            }
        }
        this.outputServos.forEachIndexed { index, pointer ->
            if (pointer != -1) {
                model.outputServoModels[index] = reader.get(pointer)
            }
        }
        model.transform = model.modelArray[0]!!.transformation
        model.buildMeshes()
        return model
    }
}