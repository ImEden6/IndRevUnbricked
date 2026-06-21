/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  net.minecraft.class_395
 *  org.lwjgl.stb.STBTTFontinfo
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import it.unimi.dsi.fastutil.ints.IntSet;
import java.nio.ByteBuffer;
import net.minecraft.class_395;
import org.lwjgl.stb.STBTTFontinfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_395.class})
public interface TrueTypeFontAccessor {
    @Accessor
    @Mutable
    public void setBuffer(ByteBuffer var1);

    @Accessor
    public STBTTFontinfo getInfo();

    @Accessor
    @Mutable
    public void setInfo(STBTTFontinfo var1);

    @Accessor
    public float getOversample();

    @Accessor
    @Mutable
    public void setOversample(float var1);

    @Accessor
    public IntSet getExcludedCharacters();

    @Accessor
    @Mutable
    public void setExcludedCharacters(IntSet var1);

    @Accessor
    public float getShiftX();

    @Accessor
    @Mutable
    public void setShiftX(float var1);

    @Accessor
    public float getShiftY();

    @Accessor
    @Mutable
    public void setShiftY(float var1);

    @Accessor
    public float getScaleFactor();

    @Accessor
    @Mutable
    public void setScaleFactor(float var1);

    @Accessor
    public float getAscent();

    @Accessor
    @Mutable
    public void setAscent(float var1);
}

