/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_277
 *  net.minecraft.class_281
 *  net.minecraft.class_284
 *  net.minecraft.class_293
 *  net.minecraft.class_5944
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.List;
import java.util.Map;
import net.minecraft.class_277;
import net.minecraft.class_281;
import net.minecraft.class_284;
import net.minecraft.class_293;
import net.minecraft.class_5944;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_5944.class})
public interface ShaderProgramAccessor {
    @Accessor
    public Map<String, Object> getSamplers();

    @Accessor
    @Mutable
    public void setSamplers(Map<String, Object> var1);

    @Accessor
    public class_277 getBlendState();

    @Accessor
    @Mutable
    public void setBlendState(class_277 var1);

    @Accessor
    public List<Integer> getLoadedAttributeIds();

    @Accessor
    public Map<String, class_284> getLoadedUniforms();

    @Accessor
    public List<class_284> getUniforms();

    @Accessor
    @Mutable
    public void setLoadedAttributeIds(List<Integer> var1);

    @Accessor
    public List<String> getAttributeNames();

    @Accessor
    @Mutable
    public void setAttributeNames(List<String> var1);

    @Accessor
    public List<String> getSamplerNames();

    @Accessor
    @Mutable
    public void setSamplerNames(List<String> var1);

    @Accessor
    @Mutable
    public void setLoadedSamplerIds(List<Integer> var1);

    @Accessor
    @Mutable
    public void setUniforms(List<class_284> var1);

    @Accessor
    @Mutable
    public void setLoadedUniformIds(List<Integer> var1);

    @Accessor
    @Mutable
    public void setLoadedUniforms(Map<String, class_284> var1);

    @Accessor
    @Mutable
    public void setGlRef(int var1);

    @Accessor
    @Mutable
    public void setName(String var1);

    @Accessor
    @Mutable
    public void setVertexShader(class_281 var1);

    @Accessor
    @Mutable
    public void setFragmentShader(class_281 var1);

    @Accessor
    @Mutable
    public void setFormat(class_293 var1);

    @Invoker(value="loadReferences")
    public void loadref();
}

