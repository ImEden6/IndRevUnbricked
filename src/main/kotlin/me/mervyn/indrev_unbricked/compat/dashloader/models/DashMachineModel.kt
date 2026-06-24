package me.mervyn.indrev_unbricked.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev_unbricked.blocks.models.MachineBakedModel
import me.mervyn.indrev_unbricked.utils.blockSpriteId
import net.minecraft.client.texture.Sprite

class DashMachineModel : DashObject<MachineBakedModel> {
    val id: String
    val defaultSprite: Int
    val overlays: IntArray
    val workingOverlays: IntArray

    constructor(
        id: String,
        defaultSprite: Int,
        overlays: IntArray,
        workingOverlays: IntArray
    ) {
        this.id = id
        this.defaultSprite = defaultSprite
        this.overlays = overlays
        this.workingOverlays = workingOverlays
    }

    constructor(model: MachineBakedModel, writer: RegistryWriter) {
        this.id = model.id
        this.defaultSprite = writer.add(model.baseSprite)
        this.overlays = model.overlays.map { writer.add(it) }.toIntArray()
        this.workingOverlays = model.workingOverlays.map { writer.add(it) }.toIntArray()
    }

    override fun export(reader: RegistryReader): MachineBakedModel {
        val model = MachineBakedModel(id)
        model.baseSprite = reader.get(defaultSprite)
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