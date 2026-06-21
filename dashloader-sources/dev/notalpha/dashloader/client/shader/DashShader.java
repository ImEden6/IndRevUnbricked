/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.blaze3d.platform.GlStateManager
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  dev.quantumfusion.hyphen.scan.annotations.DataSubclasses
 *  net.minecraft.class_284
 *  net.minecraft.class_285
 *  net.minecraft.class_293
 *  net.minecraft.class_3679
 *  net.minecraft.class_5944
 */
package dev.notalpha.dashloader.client.shader;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.shader.DashGlBlendState;
import dev.notalpha.dashloader.client.shader.DashGlUniform;
import dev.notalpha.dashloader.client.shader.DashShaderStage;
import dev.notalpha.dashloader.misc.UnsafeHelper;
import dev.notalpha.dashloader.mixin.accessor.ShaderProgramAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import dev.quantumfusion.hyphen.scan.annotations.DataSubclasses;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_284;
import net.minecraft.class_285;
import net.minecraft.class_293;
import net.minecraft.class_3679;
import net.minecraft.class_5944;

public final class DashShader
implements DashObject<class_5944> {
    public final Map<String, Sampler> samplers;
    public final String name;
    public final DashGlBlendState blendState;
    public final List<String> attributeNames;
    public final DashShaderStage vertexShader;
    public final DashShaderStage fragmentShader;
    public final int format;
    public final List<DashGlUniform> uniforms;
    public final List<String> samplerNames;
    public transient class_5944 toApply;

    public DashShader(Map<String, Sampler> samplers, String name, DashGlBlendState blendState, List<String> attributeNames, DashShaderStage vertexShader, DashShaderStage fragmentShader, int format, List<DashGlUniform> uniforms, List<String> samplerNames) {
        this.samplers = samplers;
        this.name = name;
        this.blendState = blendState;
        this.attributeNames = attributeNames;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.format = format;
        this.uniforms = uniforms;
        this.samplerNames = samplerNames;
    }

    public DashShader(class_5944 shader, RegistryWriter writer) {
        ShaderProgramAccessor shaderAccess = (ShaderProgramAccessor)shader;
        this.samplers = new LinkedHashMap<String, Sampler>();
        shaderAccess.getSamplers().forEach((s, o) -> this.samplers.put((String)s, new Sampler(o)));
        this.name = shader.method_35787();
        this.blendState = new DashGlBlendState(shaderAccess.getBlendState());
        this.attributeNames = shaderAccess.getAttributeNames();
        this.vertexShader = new DashShaderStage(shader.method_1274());
        this.fragmentShader = new DashShaderStage(shader.method_1278());
        this.format = writer.add(shader.method_35786());
        this.uniforms = new ArrayList<DashGlUniform>();
        Map<String, class_284> loadedUniforms = shaderAccess.getLoadedUniforms();
        shaderAccess.getUniforms().forEach(glUniform -> this.uniforms.add(new DashGlUniform((class_284)glUniform, loadedUniforms.containsKey(glUniform.method_1298()))));
        this.samplerNames = shaderAccess.getSamplerNames();
    }

    @Override
    public class_5944 export(RegistryReader reader) {
        this.toApply = UnsafeHelper.allocateInstance(class_5944.class);
        ShaderProgramAccessor shaderAccess = (ShaderProgramAccessor)this.toApply;
        shaderAccess.setLoadedSamplerIds(new ArrayList<Integer>());
        shaderAccess.setLoadedUniformIds(new ArrayList<Integer>());
        shaderAccess.setLoadedAttributeIds(new ArrayList<Integer>());
        shaderAccess.setSamplerNames(new ArrayList<String>(this.samplerNames));
        shaderAccess.setName(this.name);
        shaderAccess.setFormat((class_293)reader.get(this.format));
        HashMap<String, Object> samplersOut = new HashMap<String, Object>();
        this.samplers.forEach((s, o) -> samplersOut.put((String)s, o.sampler));
        shaderAccess.setSamplers(samplersOut);
        shaderAccess.setAttributeNames(new ArrayList<String>(this.attributeNames));
        ArrayList<class_284> uniforms = new ArrayList<class_284>();
        shaderAccess.setUniforms(uniforms);
        HashMap<String, class_284> uniformsOut = new HashMap<String, class_284>();
        this.uniforms.forEach(dashGlUniform -> {
            class_284 uniform = dashGlUniform.export(this.toApply);
            uniforms.add(uniform);
            if (dashGlUniform.loaded) {
                uniformsOut.put(dashGlUniform.name, uniform);
            }
        });
        shaderAccess.setLoadedUniforms(uniformsOut);
        this.toApply.method_1279();
        this.toApply.field_29470 = uniformsOut.get("ModelViewMat");
        this.toApply.field_29471 = uniformsOut.get("ProjMat");
        this.toApply.field_36323 = uniformsOut.get("IViewRotMat");
        this.toApply.field_29472 = uniformsOut.get("TextureMat");
        this.toApply.field_29473 = uniformsOut.get("ScreenSize");
        this.toApply.field_29474 = uniformsOut.get("ColorModulator");
        this.toApply.field_29475 = uniformsOut.get("Light0_Direction");
        this.toApply.field_29476 = uniformsOut.get("Light1_Direction");
        this.toApply.field_29477 = uniformsOut.get("FogStart");
        this.toApply.field_29478 = uniformsOut.get("FogEnd");
        this.toApply.field_29479 = uniformsOut.get("FogColor");
        this.toApply.field_36373 = uniformsOut.get("FogShape");
        this.toApply.field_29480 = uniformsOut.get("LineWidth");
        this.toApply.field_29481 = uniformsOut.get("GameTime");
        this.toApply.field_29482 = uniformsOut.get("ChunkOffset");
        return this.toApply;
    }

    @Override
    public void postExport(RegistryReader reader) {
        ShaderProgramAccessor shaderAccess = (ShaderProgramAccessor)this.toApply;
        shaderAccess.setBlendState(this.blendState.export());
        shaderAccess.setVertexShader(this.vertexShader.exportProgram());
        shaderAccess.setFragmentShader(this.fragmentShader.exportProgram());
        List<Integer> loadedAttributeIds = shaderAccess.getLoadedAttributeIds();
        int programId = GlStateManager.glCreateProgram();
        shaderAccess.setGlRef(programId);
        if (this.attributeNames != null) {
            ImmutableList names = this.toApply.method_35786().method_34445();
            for (int i = 0; i < names.size(); ++i) {
                String attributeName = (String)names.get(i);
                class_284.method_34419((int)programId, (int)i, (CharSequence)attributeName);
                loadedAttributeIds.add(i);
            }
        }
        class_285.method_1307((class_3679)this.toApply);
        shaderAccess.loadref();
    }

    public static class Sampler {
        public final @DataNullable @DataSubclasses(value={Integer.class, String.class}) Object sampler;

        public Sampler(Object sampler) {
            this.sampler = sampler;
        }
    }
}

