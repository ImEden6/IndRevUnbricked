/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  net.minecraft.class_281
 *  net.minecraft.class_281$class_282
 *  org.apache.commons.lang3.StringUtils
 */
package dev.notalpha.dashloader.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.shader.ShaderModule;
import dev.notalpha.dashloader.mixin.accessor.ShaderStageAccessor;
import java.util.List;
import java.util.Map;
import net.minecraft.class_281;
import org.apache.commons.lang3.StringUtils;

public final class DashShaderStage {
    public final class_281.class_282 shaderType;
    public final String name;
    public final List<String> shader;

    public DashShaderStage(class_281.class_282 shaderType, String name, List<String> shader) {
        this.shaderType = shaderType;
        this.name = name;
        this.shader = shader;
    }

    public DashShaderStage(class_281 program) {
        ShaderStageAccessor access = (ShaderStageAccessor)program;
        this.shaderType = access.getType();
        this.name = program.method_1280();
        List shader = (List)ShaderModule.WRITE_PROGRAM_SOURCES.get(CacheStatus.SAVE).get(access.getGlRef());
        if (shader == null) {
            throw new RuntimeException();
        }
        this.shader = shader;
    }

    public int createProgram(class_281.class_282 type) {
        int id = GlStateManager.glCreateShader((int)((ShaderStageAccessor.TypeAccessor)type).getGlType());
        GlStateManager.glShaderSource((int)id, this.shader);
        GlStateManager.glCompileShader((int)id);
        if (GlStateManager.glGetShaderi((int)id, (int)35713) == 0) {
            String errorString = StringUtils.trim((String)GlStateManager.glGetShaderInfoLog((int)id, (int)32768));
            throw new RuntimeException("Couldn't compile " + type.method_1286() + " : " + errorString);
        }
        return id;
    }

    public class_281 exportProgram() {
        Map loadedShaders = this.shaderType.method_1289();
        class_281 program = ShaderStageAccessor.create(this.shaderType, this.createProgram(this.shaderType), this.name);
        loadedShaders.put(this.name, program);
        return program;
    }
}

