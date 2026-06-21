/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_309
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.main;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.client.DashLoaderClient;
import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_309.class})
public class KeyboardMixin {
    private boolean shiftHeld = false;

    @Inject(method={"processF3"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;", shift=At.Shift.BEFORE)})
    private void f3tReloadWorld(int key, CallbackInfoReturnable<Boolean> cir) {
        if (!this.shiftHeld) {
            DashLoader.LOG.info("Clearing cache.");
            DashLoaderClient.CACHE.remove();
        }
    }

    @Inject(method={"onKey"}, at={@At(value="HEAD")})
    private void keyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        this.shiftHeld = action != 0 && modifiers == 1;
    }
}

