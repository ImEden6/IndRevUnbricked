/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StaticTask
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_368
 *  net.minecraft.class_4011
 *  net.minecraft.class_425
 *  net.minecraft.class_442
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.notalpha.dashloader.mixin.main;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.DashLoaderClient;
import dev.notalpha.dashloader.client.ui.DashToast;
import dev.notalpha.dashloader.client.ui.DashToastState;
import dev.notalpha.dashloader.client.ui.DashToastStatus;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.misc.ProfilerUtil;
import dev.notalpha.taski.builtin.StaticTask;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_368;
import net.minecraft.class_4011;
import net.minecraft.class_425;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_425.class}, priority=69420)
public class SplashScreenMixin {
    @Shadow
    @Final
    private class_310 field_18217;
    @Shadow
    private long field_17771;
    @Shadow
    @Final
    private class_4011 field_17767;
    @Mutable
    @Shadow
    @Final
    private boolean field_18219;

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/Util;getMeasuringTimeMs()J", shift=At.Shift.BEFORE, ordinal=1)})
    private void done(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.field_18217.method_18502(null);
        if (this.field_18217.field_1755 != null && this.field_18217.field_1755 instanceof class_442) {
            this.field_18217.field_1755 = new class_442(false);
        }
        DashLoader.LOG.info("Minecraft reloaded in {}", (Object)ProfilerUtil.getTimeStringFromStart(ProfilerUtil.RELOAD_START));
        Cache cache = DashLoaderClient.CACHE;
        if (DashLoaderClient.CACHE.getStatus() == CacheStatus.SAVE && this.field_18217.method_1566().method_1997(DashToast.class, class_368.field_2208) == null) {
            DashToastState rawState;
            if (ConfigHandler.INSTANCE.config.showCachingToast) {
                DashToast toast = new DashToast();
                this.field_18217.method_1566().method_1999((class_368)toast);
                rawState = toast.state;
            } else {
                rawState = new DashToastState();
            }
            Thread thread = new Thread(() -> {
                DashToastState state;
                DashToastState finalState = state = rawState;
                state.setStatus(DashToastStatus.PROGRESS);
                long start = System.currentTimeMillis();
                boolean save = cache.save(stepTask -> {
                    finalState.task = stepTask;
                });
                if (save) {
                    state.setOverwriteText("Created cache in " + ProfilerUtil.getTimeStringFromStart(start));
                    state.setStatus(DashToastStatus.DONE);
                } else {
                    if (!ConfigHandler.INSTANCE.config.showCachingToast) {
                        DashToast toast = new DashToast();
                        this.field_18217.method_1566().method_1999((class_368)toast);
                        state = toast.state;
                    }
                    state.setOverwriteText("Internal error, Please check logs.");
                    state.task = new StaticTask("Crash", 0.0f);
                    state.setStatus(DashToastStatus.CRASHED);
                }
                cache.reset();
                state.setDone();
            });
            thread.setName("dashloader-thread");
            thread.start();
        } else {
            cache.reset();
        }
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/resource/ResourceReload;isComplete()Z", shift=At.Shift.BEFORE)})
    private void removeMinimumTime(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.field_17771 == -1L && this.field_17767.method_18787()) {
            this.field_18219 = false;
        }
    }
}

