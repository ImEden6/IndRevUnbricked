/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7766$class_7767
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_7766;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_7766.class_7767.class})
public interface SpriteLoaderStitchResultAccessor {
    @Accessor
    public int getWidth();

    @Accessor
    public int getHeight();

    @Accessor
    public int getMipLevel();
}

