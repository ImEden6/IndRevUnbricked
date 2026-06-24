package me.mervyn.indrev_unbricked.compat.dashloader

import dev.notalpha.dashloader.api.DashEntrypoint
import dev.notalpha.dashloader.api.cache.CacheFactory
import me.mervyn.indrev_unbricked.compat.dashloader.models.*

class IndRevDashLoaderPlugin : DashEntrypoint {
    override fun onDashLoaderInit(factory: CacheFactory) {
        factory.addDashObject(DashCableModel::class.java)
        factory.addDashObject(DashFluidPipeModel::class.java)
        factory.addDashObject(DashItemPipeModel::class.java)
        factory.addDashObject(DashLazuliFluxContainerModel::class.java)
        factory.addDashObject(DashMachineModel::class.java)
        factory.addDashObject(DashMinerModel::class.java)
        factory.addDashObject(DashTankModel::class.java)
    }
}
