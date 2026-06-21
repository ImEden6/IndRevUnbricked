/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2966
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.notalpha.dashloader.mixin.main;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.misc.ProfilerUtil;
import net.minecraft.class_2966;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_2966.class}, priority=-69)
public class BootstrapMixin {
    private static long BOOTSTRAP_START = -1L;

    @Inject(method={"initialize"}, at={@At(value="HEAD")})
    private static void timeStart(CallbackInfo ci) {
        BOOTSTRAP_START = System.currentTimeMillis();
    }

    @Inject(method={"initialize"}, at={@At(value="TAIL")})
    private static void timeStop(CallbackInfo ci) {
        DashLoader.LOG.info("Minecraft bootstrap in {}", (Object)ProfilerUtil.getTimeStringFromStart(BOOTSTRAP_START));
    }
}

