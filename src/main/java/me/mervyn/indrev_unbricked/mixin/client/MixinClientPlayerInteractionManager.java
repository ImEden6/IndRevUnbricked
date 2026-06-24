package me.mervyn.indrev_unbricked.mixin.client;

import me.mervyn.indrev_unbricked.registry.IRItemRegistry;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    // TODO: Re-enable or remove this mixin — currently dead (entire body commented out)
   /* @Redirect(method = "isCurrentlyBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;canCombine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean indrev_fixGamerAxe(ItemStack left, ItemStack right) {
        if (left.getItem() == IRItemRegistry.INSTANCE.getGAMER_AXE_ITEM() && left.getItem() == right.getItem())
            return true;
        return ItemStack.canCombine(left, right);
    }*/
}
