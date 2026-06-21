/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.notalpha.dashloader.mixin.option.cache.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.shader.ShaderModule;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GlStateManager.class})
public class GlStateManagerMixin {
    @Inject(method={"glShaderSource"}, at={@At(value="HEAD")})
    private static void glShaderSourceInject(int shader, List<String> strings, CallbackInfo ci) {
        ShaderModule.WRITE_PROGRAM_SOURCES.visit(CacheStatus.SAVE, map -> map.put(shader, (Object)strings));
    }
}

