/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3300
 *  net.minecraft.class_3695
 *  net.minecraft.class_4008
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.option.cache;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.splash.SplashModule;
import java.util.Collection;
import java.util.List;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import net.minecraft.class_4008;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_4008.class})
public class SplashTextResourceSupplierMixin {
    @Inject(method={"prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;"}, at={@At(value="HEAD")}, cancellable=true)
    private void applySplashCache(class_3300 resourceManager, class_3695 profiler, CallbackInfoReturnable<List<String>> cir) {
        SplashModule.TEXTS.visit(CacheStatus.LOAD, arg_0 -> cir.setReturnValue(arg_0));
    }

    @Inject(method={"prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;"}, at={@At(value="RETURN")})
    private void stealSplashCache(class_3300 resourceManager, class_3695 profiler, CallbackInfoReturnable<List<String>> cir) {
        SplashModule.TEXTS.visit(CacheStatus.SAVE, strings -> {
            strings.clear();
            strings.addAll((Collection)cir.getReturnValue());
        });
    }
}

