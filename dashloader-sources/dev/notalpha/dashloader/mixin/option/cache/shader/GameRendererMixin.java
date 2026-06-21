/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_293
 *  net.minecraft.class_5912
 *  net.minecraft.class_5944
 *  net.minecraft.class_757
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package dev.notalpha.dashloader.mixin.option.cache.shader;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.shader.ShaderModule;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.class_293;
import net.minecraft.class_5912;
import net.minecraft.class_5944;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_757.class}, priority=69)
public abstract class GameRendererMixin {
    @Redirect(method={"loadPrograms"}, at=@At(value="NEW", target="(Lnet/minecraft/resource/ResourceFactory;Ljava/lang/String;Lnet/minecraft/client/render/VertexFormat;)Lnet/minecraft/client/gl/ShaderProgram;"))
    private class_5944 shaderCreation(class_5912 factory, String name, class_293 format) throws IOException {
        class_5944 shader;
        HashMap<String, class_5944> shaders = ShaderModule.SHADERS.get(CacheStatus.LOAD);
        if (shaders != null && (shader = shaders.get(name)) != null) {
            return shader;
        }
        shader = new class_5944(factory, name, format);
        ShaderModule.SHADERS.visit(CacheStatus.SAVE, map -> map.put(name, shader));
        return shader;
    }
}

