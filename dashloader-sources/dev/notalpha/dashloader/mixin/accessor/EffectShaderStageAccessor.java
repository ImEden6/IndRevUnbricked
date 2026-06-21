/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_281$class_282
 *  net.minecraft.class_5937
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_281;
import net.minecraft.class_5937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_5937.class})
public interface EffectShaderStageAccessor {
    @Invoker(value="<init>")
    public static class_5937 create(class_281.class_282 shaderType, int shaderRef, String name) {
        throw new AssertionError();
    }
}

