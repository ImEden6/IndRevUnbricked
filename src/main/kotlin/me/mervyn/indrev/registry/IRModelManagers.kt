package me.mervyn.indrev.registry

import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blocks.machine.DrillHeadModel
import me.mervyn.indrev.blocks.models.PumpPipeBakedModel
import me.mervyn.indrev.blocks.models.pipes.CableModel
import me.mervyn.indrev.blocks.models.pipes.FluidPipeModel
import me.mervyn.indrev.blocks.models.pipes.ItemPipeModel
import me.mervyn.indrev.items.models.TankItemBakedModel
import me.mervyn.indrev.utils.EmptyModel
import me.mervyn.indrev.utils.hide
import me.mervyn.indrev.utils.identifier
import net.fabricmc.fabric.api.client.model.ExtraModelProvider
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.function.Consumer

object IRModelManagers : ModelVariantProvider, ExtraModelProvider {

    private val CABLE_MODELS = arrayOf(
        CableModel(Tier.MK1), CableModel(Tier.MK2), CableModel(Tier.MK3), CableModel(Tier.MK4)
    )

    private val ITEM_PIPE_MODELS = arrayOf(
        ItemPipeModel(Tier.MK1), ItemPipeModel(Tier.MK2), ItemPipeModel(Tier.MK3), ItemPipeModel(Tier.MK4)
    )

    private val FLUID_PIPE_MODELS = arrayOf(
        FluidPipeModel(Tier.MK1), FluidPipeModel(Tier.MK2), FluidPipeModel(Tier.MK3), FluidPipeModel(Tier.MK4)
    )

    override fun loadModelVariant(resourceId: ModelIdentifier, ctx: ModelProviderContext?): UnbakedModel? {
        if (resourceId.namespace != "indrev_unbricked") return null
        val path = resourceId.path
        val variant = resourceId.variant
        val id = Identifier(resourceId.namespace, resourceId.path)
        return when {
            hide(id) -> EmptyModel
            path == "drill_head" -> DrillHeadModel(resourceId.variant)
            path == "pump_pipe" -> PumpPipeBakedModel()
            path == "tank" && variant == "inventory" -> TankItemBakedModel()
            path.startsWith("cable_mk") -> CABLE_MODELS[path.last().toString().toInt() - 1]
            path.startsWith("item_pipe_mk") -> ITEM_PIPE_MODELS[path.last().toString().toInt() - 1]
            path.startsWith("fluid_pipe_mk") -> FLUID_PIPE_MODELS[path.last().toString().toInt() - 1]
            MachineRegistry.MAP.containsKey(id) -> MachineRegistry.MAP[id]?.modelProvider?.get(Tier.values()[(path.last().toString().toIntOrNull() ?: 4) - 1])?.invoke(id.path.replace("_creative", "_mk4"))
            else -> return null
        }
    }

    override fun provideExtraModels(manager: ResourceManager?, out: Consumer<Identifier>) {
        out.accept(ModelIdentifier(identifier("drill_head"), "stone"))
        out.accept(ModelIdentifier(identifier("drill_head"), "iron"))
        out.accept(ModelIdentifier(identifier("drill_head"), "diamond"))
        out.accept(ModelIdentifier(identifier("drill_head"), "netherite"))
        out.accept(ModelIdentifier(identifier("pump_pipe"), ""))
        out.accept(ModelIdentifier(identifier("composting"), ""))
    }
}
