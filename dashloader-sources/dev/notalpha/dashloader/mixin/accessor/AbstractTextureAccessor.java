/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1044
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1044;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1044.class})
public interface AbstractTextureAccessor {
    @Accessor
    public boolean getBilinear();

    @Accessor
    public void setBilinear(boolean var1);

    @Accessor
    public boolean getMipmap();

    @Accessor
    public void setMipmap(boolean var1);
}

