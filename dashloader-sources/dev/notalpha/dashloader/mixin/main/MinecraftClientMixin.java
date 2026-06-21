/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.main;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.DashLoaderClient;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_310.class})
public abstract class MinecraftClientMixin {
    @Shadow
    protected abstract void method_1523(boolean var1);

    @Inject(method={"reloadResources()Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;reloadResources(Z)Ljava/util/concurrent/CompletableFuture;")})
    private void requestReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        DashLoaderClient.NEEDS_RELOAD = true;
    }

    @Inject(method={"reloadResources(Z)Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="RETURN")})
    private void reloadComplete(boolean thing, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ((CompletableFuture)cir.getReturnValue()).thenRun(() -> {
            if (DashLoaderClient.CACHE.getStatus() != CacheStatus.SAVE) {
                DashLoaderClient.CACHE.reset();
            }
        });
    }
}

