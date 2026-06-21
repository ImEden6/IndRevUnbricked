/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1092
 *  net.minecraft.class_1092$class_7779
 *  net.minecraft.class_2960
 *  net.minecraft.class_3695
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.notalpha.dashloader.mixin.option.cache.model;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.model.ModelModule;
import java.util.Map;
import net.minecraft.class_1087;
import net.minecraft.class_1092;
import net.minecraft.class_2960;
import net.minecraft.class_3695;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1092.class}, priority=69420)
public abstract class BakedModelManagerOverride {
    @Shadow
    private Map<class_2960, class_1087> field_5408;

    @Inject(method={"upload"}, at={@At(value="TAIL")})
    private void yankAssets(class_1092.class_7779 bakingResult, class_3695 profiler, CallbackInfo ci) {
        ModelModule.MODELS_SAVE.visit(CacheStatus.SAVE, map -> {
            DashLoader.LOG.info("Yanking Minecraft Assets");
            map.putAll(this.field_5408);
        });
    }
}

