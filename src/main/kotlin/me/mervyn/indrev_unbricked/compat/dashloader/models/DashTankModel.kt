package me.mervyn.indrev_unbricked.compat.dashloader.models

import dev.notalpha.dashloader.api.DashObject
import dev.notalpha.dashloader.api.registry.RegistryReader
import dev.notalpha.dashloader.api.registry.RegistryWriter
import me.mervyn.indrev_unbricked.items.models.TankItemBakedModel

class DashTankModel : DashObject<TankItemBakedModel> {

    constructor()

    constructor(model: TankItemBakedModel, writer: RegistryWriter)

    override fun export(reader: RegistryReader): TankItemBakedModel {
        return TankItemBakedModel()
    }
}