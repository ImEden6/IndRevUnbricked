/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_3258
 *  net.minecraft.class_3262
 *  net.minecraft.class_3283
 *  net.minecraft.class_3288
 *  net.minecraft.class_3304
 *  net.minecraft.class_3902
 *  net.minecraft.class_4011
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.main;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.client.DashLoaderClient;
import dev.notalpha.dashloader.misc.ProfilerUtil;
import dev.notalpha.dashloader.mixin.accessor.ZipResourcePackAccessor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.class_310;
import net.minecraft.class_3258;
import net.minecraft.class_3262;
import net.minecraft.class_3283;
import net.minecraft.class_3288;
import net.minecraft.class_3304;
import net.minecraft.class_3902;
import net.minecraft.class_4011;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_3304.class})
public class ReloadableResourceManagerImplMixin {
    @Inject(method={"reload"}, at={@At(value="RETURN", shift=At.Shift.BEFORE)})
    private void reloadDash(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<class_3902> initialStage, List<class_3262> packs, CallbackInfoReturnable<class_4011> cir) {
        ProfilerUtil.RELOAD_START = System.currentTimeMillis();
        class_3283 manager = class_310.method_1551().method_1520();
        ArrayList<Object> values = new ArrayList<Object>();
        for (class_3262 pack : packs) {
            if (!Objects.equals(pack.method_14409(), "server") || !(pack instanceof class_3258)) continue;
            class_3258 zipResourcePack = (class_3258)pack;
            ZipResourcePackAccessor zipPack = (ZipResourcePackAccessor)zipResourcePack;
            Path path = zipPack.getBackingZipFile().toPath();
            values.add(path.toString());
        }
        for (class_3288 profile : manager.method_14444()) {
            if (profile == null || Objects.equals(profile.method_14463(), "server")) continue;
            values.add(profile.method_14463() + "/");
        }
        String hash = DigestUtils.md5Hex((String)((Object)values).toString()).toUpperCase();
        DashLoader.LOG.info("Hash changed to " + hash);
        DashLoaderClient.CACHE.load(hash);
    }
}

