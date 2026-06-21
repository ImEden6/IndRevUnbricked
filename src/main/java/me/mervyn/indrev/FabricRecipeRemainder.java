package me.mervyn.indrev;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;

public interface FabricRecipeRemainder {
    ItemStack getRemainder(ItemStack stack, RecipeInputInventory craftingInventory, PlayerEntity playerEntity);
}