/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_284
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.class_284;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_284.class})
public interface GlUniformAccessor {
    @Accessor
    @Mutable
    public void setIntData(IntBuffer var1);

    @Accessor
    public IntBuffer getIntData();

    @Accessor
    public FloatBuffer getFloatData();

    @Accessor
    @Mutable
    public void setFloatData(FloatBuffer var1);

    @Accessor
    @Mutable
    public void setName(String var1);
}

