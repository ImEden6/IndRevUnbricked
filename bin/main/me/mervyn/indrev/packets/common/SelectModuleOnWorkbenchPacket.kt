package me.mervyn.indrev.packets.common

import me.mervyn.indrev.blockentities.modularworkbench.ModularWorkbenchBlockEntity
import me.mervyn.indrev.gui.screenhandlers.machines.ModularWorkbenchScreenHandler
import me.mervyn.indrev.recipes.machines.ModuleRecipe
import me.mervyn.indrev.utils.getRecipes
import me.mervyn.indrev.utils.identifier
import me.mervyn.indrev.utils.isLoaded
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object SelectModuleOnWorkbenchPacket  {

    val MODULE_SELECT_PACKET = identifier("module_select_packet") 

     fun register() {
        ServerPlayNetworking.registerGlobalReceiver(MODULE_SELECT_PACKET) { server, player, _, buf, _ ->
            val syncId = buf.readInt()
            val recipeId = buf.readIdentifier()
            val pos = buf.readBlockPos()
            val screenHandler =
                player.currentScreenHandler as? ModularWorkbenchScreenHandler ?: return@registerGlobalReceiver
            if (syncId != screenHandler.syncId) return@registerGlobalReceiver
            server.execute {
                val world = player.world
                if (world.isLoaded(pos)) {
                    val recipe = server.recipeManager.getRecipes(ModuleRecipe.TYPE)[recipeId]!!
                    screenHandler.layoutSlots(recipe)
                    val blockEntity = world.getBlockEntity(pos) as? ModularWorkbenchBlockEntity ?: return@execute
                    blockEntity.selectedRecipe = recipeId
                    blockEntity.markDirty()
                    blockEntity.sync()
                }
            }
        }

    }
}