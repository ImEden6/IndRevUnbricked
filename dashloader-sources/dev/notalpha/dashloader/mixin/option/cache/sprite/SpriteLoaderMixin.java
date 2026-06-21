/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_3300
 *  net.minecraft.class_7766
 *  net.minecraft.class_7766$class_7767
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.option.cache.sprite;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.sprite.SpriteModule;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_7766;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_7766.class})
public final class SpriteLoaderMixin {
    @Shadow
    @Final
    private class_2960 field_40549;

    @Inject(method={"load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;ILjava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="RETURN")}, cancellable=true)
    private void dashloaderWrite(class_3300 resourceManager, class_2960 identifier, int i, Executor executor, CallbackInfoReturnable<CompletableFuture<class_7766.class_7767>> cir) {
        SpriteModule.ATLASES.visit(CacheStatus.SAVE, map -> {
            SpriteModule.ATLAS_IDS.get(CacheStatus.SAVE).put(this.field_40549, identifier);
            cir.setReturnValue((Object)((CompletableFuture)cir.getReturnValue()).thenApply(stitchResult -> {
                map.put(identifier, stitchResult);
                return stitchResult;
            }));
        });
    }

    @Inject(method={"load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;ILjava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="HEAD")}, cancellable=true)
    private void dashloaderRead(class_3300 resourceManager, class_2960 identifier, int m, Executor executor, CallbackInfoReturnable<CompletableFuture<class_7766.class_7767>> cir) {
        SpriteModule.ATLASES.visit(CacheStatus.LOAD, map -> {
            class_7766.class_7767 cached = (class_7766.class_7767)map.get(identifier);
            if (cached != null) {
                int mipLevel = cached.comp_1042();
                CompletableFuture<Object> completableFuture = mipLevel > 0 ? CompletableFuture.runAsync(() -> cached.comp_1044().values().forEach(sprite -> sprite.method_45851().method_45808(mipLevel)), executor) : CompletableFuture.completedFuture(null);
                cir.setReturnValue(CompletableFuture.completedFuture(new class_7766.class_7767(cached.comp_1040(), cached.comp_1041(), mipLevel, cached.comp_1043(), cached.comp_1044(), completableFuture)));
                cir.cancel();
            }
        });
    }
}

