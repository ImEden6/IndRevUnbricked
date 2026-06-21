package me.mervyn.indrev.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev.items.models.TankItemBakedModel

class DashTankModel : DashObject<TankItemBakedModel> {

    constructor()

    constructor(model: TankItemBakedModel, writer: RegistryWriter)

    override fun export(reader: RegistryReader): TankItemBakedModel {
        return TankItemBakedModel()
    }
}