package me.mervyn.indrev.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev.blocks.models.MinerBakedModel
import me.mervyn.indrev.utils.blockSpriteId
import net.minecraft.client.texture.Sprite

class DashMinerModel : DashObject<MinerBakedModel> {
    val id: String
    val defaultSprite: Int
    val overlays: IntArray
    val workingOverlays: IntArray
    val screenSprite: Int

    constructor(
        id: String,
        defaultSprite: Int,
        overlays: IntArray,
        workingOverlays: IntArray,
        screenSprite: Int
    ) {
        this.id = id
        this.defaultSprite = defaultSprite
        this.overlays = overlays
        this.workingOverlays = workingOverlays
        this.screenSprite = screenSprite
    }

    constructor(model: MinerBakedModel, writer: RegistryWriter) {
        this.id = model.id
        this.defaultSprite = writer.add(model.baseSprite)
        this.overlays = model.overlays.map { writer.add(it) }.toIntArray()
        this.workingOverlays = model.workingOverlays.map { writer.add(it) }.toIntArray()
        this.screenSprite = writer.add(model.screenSprite)
    }

    override fun export(reader: RegistryReader): MinerBakedModel {
        val model = MinerBakedModel(id)
        model.baseSprite = reader.get(defaultSprite)
        model.screenSprite = reader.get(screenSprite)
        overlays.indices.forEach { _ -> model.overlayIds.add(blockSpriteId("")) }
        workingOverlays.indices.forEach { _ -> model.workingOverlayIds.add(blockSpriteId("")) }
        model.overlays.indices.forEach { index ->
            val sprite: Sprite = reader.get(overlays[index])
            model.overlays[index] = sprite
            if (model.isEmissive(sprite)) model.emissives.add(sprite)
        }
        model.workingOverlays.indices.forEach { index ->
            val sprite: Sprite = reader.get(workingOverlays[index])
            model.workingOverlays[index] = sprite
            if (model.isEmissive(sprite)) model.emissives.add(sprite)
        }
        model.buildDefaultMesh()
        model.buildWorkingStateMesh()
        return model
    }
}