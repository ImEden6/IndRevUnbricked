/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_281
 *  net.minecraft.class_281$class_282
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_281;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_281.class})
public interface ShaderStageAccessor {
    @Invoker(value="<init>")
    public static class_281 create(class_281.class_282 shaderType, int shaderRef, String name) {
        throw new AssertionError();
    }

    @Accessor
    public class_281.class_282 getType();

    @Accessor
    public int getGlRef();

    @Mixin(value={class_281.class_282.class})
    public static interface TypeAccessor {
        @Accessor
        public int getGlType();
    }
}

