package me.mervyn.indrev_unbricked.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev_unbricked.blocks.models.LazuliFluxContainerBakedModel
import net.minecraft.client.texture.Sprite

class DashLazuliFluxContainerModel : DashObject<LazuliFluxContainerBakedModel> {
    val id: String
    val defaultSprite: Int
    val overlays: IntArray

    constructor(
        id: String,
        defaultSprite: Int,
        overlays: IntArray
    ) {
        this.id = id
        this.defaultSprite = defaultSprite
        this.overlays = overlays
    }

    constructor(model: LazuliFluxContainerBakedModel, writer: RegistryWriter) {
        this.id = model.id
        this.defaultSprite = writer.add(model.baseSprite)
        this.overlays = model.overlays.map { writer.add(it) }.toIntArray()
    }

    override fun export(reader: RegistryReader): LazuliFluxContainerBakedModel {
        val model = LazuliFluxContainerBakedModel(id)
        model.baseSprite = reader.get(defaultSprite)
        model.overlays.indices.forEach { index ->
            val sprite: Sprite = reader.get(overlays[index])
            model.overlays[index] = sprite
            if (model.isEmissive(sprite)) model.emissives.add(sprite)
        }
        model.buildDefaultMesh()
        return model
    }
}