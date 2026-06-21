/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1088
 *  net.minecraft.class_1088$class_7777
 *  net.minecraft.class_1100
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_324
 *  net.minecraft.class_3695
 *  net.minecraft.class_4730
 *  net.minecraft.class_793
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.notalpha.dashloader.mixin.option.cache.model;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.model.ModelModule;
import dev.notalpha.dashloader.client.model.fallback.UnbakedBakedModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.class_1058;
import net.minecraft.class_1088;
import net.minecraft.class_1100;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_324;
import net.minecraft.class_3695;
import net.minecraft.class_4730;
import net.minecraft.class_793;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1088.class}, priority=69420)
public abstract class ModelLoaderMixin {
    @Mutable
    @Shadow
    @Final
    private Map<class_2960, class_1100> field_5376;
    @Mutable
    @Shadow
    @Final
    private Map<class_2960, class_1100> field_5394;

    @Shadow
    protected abstract void method_4716(class_2680 var1);

    @Inject(method={"<init>"}, at={@At(value="INVOKE_STRING", target="Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args={"ldc=static_definitions"}, shift=At.Shift.AFTER)})
    private void injectLoadedModels(class_324 blockColors, class_3695 profiler, Map<class_2960, class_793> jsonUnbakedModels, Map<class_2960, List<class_1088.class_7777>> blockStates, CallbackInfo ci) {
        ModelModule.MODELS_LOAD.visit(CacheStatus.LOAD, dashModels -> {
            int total = dashModels.size();
            this.field_5376.keySet().forEach(dashModels::remove);
            this.field_5394.keySet().forEach(dashModels::remove);
            DashLoader.LOG.info("Injecting {}/{} Cached Models", (Object)dashModels.size(), (Object)total);
            this.field_5376.putAll((Map<class_2960, class_1100>)dashModels);
            this.field_5394.putAll((Map<class_2960, class_1100>)dashModels);
        });
    }

    @Redirect(method={"<init>"}, at=@At(value="INVOKE", target="Ljava/util/Iterator;hasNext()Z", ordinal=0))
    private boolean loadMissingModels(Iterator instance) {
        HashMap<class_2680, class_2960> map = ModelModule.MISSING_READ.get(CacheStatus.LOAD);
        if (map != null) {
            for (class_2680 blockState : map.keySet()) {
                this.method_4716(blockState);
            }
            DashLoader.LOG.info("Loaded {} unsupported models.", (Object)map.size());
            return false;
        }
        return instance.hasNext();
    }

    @Inject(method={"bake"}, at={@At(value="HEAD")})
    private void countModels(BiFunction<class_2960, class_4730, class_1058> spriteLoader, CallbackInfo ci) {
        if (ModelModule.MODELS_LOAD.active(CacheStatus.LOAD)) {
            int cachedModels = 0;
            int fallbackModels = 0;
            for (class_1100 value : this.field_5394.values()) {
                if (value instanceof UnbakedBakedModel) {
                    ++cachedModels;
                    continue;
                }
                ++fallbackModels;
            }
            long totalModels = cachedModels + fallbackModels;
            DashLoader.LOG.info("{}% Cache coverage", (Object)((int)((float)cachedModels / (float)totalModels * 100.0f)));
            DashLoader.LOG.info("with {} Fallback models", (Object)fallbackModels);
            DashLoader.LOG.info("and  {} Cached models", (Object)cachedModels);
        }
    }
}

