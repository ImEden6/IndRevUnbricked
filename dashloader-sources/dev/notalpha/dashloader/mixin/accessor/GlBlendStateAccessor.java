/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_277
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_277;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_277.class})
public interface GlBlendStateAccessor {
    @Invoker(value="<init>")
    public static class_277 create(boolean separateBlend, boolean blendDisabled, int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, int mode) {
        throw new AssertionError();
    }

    @Accessor
    public int getSrcRgb();

    @Accessor
    public int getSrcAlpha();

    @Accessor
    public int getDstRgb();

    @Accessor
    public int getDstAlpha();

    @Accessor
    public int getMode();

    @Accessor
    public boolean getSeparateBlend();

    @Accessor
    public boolean getBlendDisabled();
}

