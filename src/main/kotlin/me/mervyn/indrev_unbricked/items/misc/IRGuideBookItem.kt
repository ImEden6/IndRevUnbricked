package me.mervyn.indrev_unbricked.items.misc

import me.mervyn.indrev_unbricked.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import vazkii.patchouli.api.PatchouliAPI

class IRGuideBookItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            PatchouliAPI.get().openBookGUI(user as ServerPlayerEntity, identifier("indrev_unbricked"))
            return TypedActionResult.success(user.getStackInHand(hand))
        }
        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}