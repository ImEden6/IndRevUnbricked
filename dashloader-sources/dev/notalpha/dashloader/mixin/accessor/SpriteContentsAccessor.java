/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_2960
 *  net.minecraft.class_7764
 *  net.minecraft.class_7764$class_5790
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1011;
import net.minecraft.class_2960;
import net.minecraft.class_7764;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_7764.class})
public interface SpriteContentsAccessor {
    @Accessor
    public class_1011 getImage();

    @Accessor
    public class_7764.class_5790 getAnimation();

    @Accessor
    public class_1011[] getMipmapLevelsImages();

    @Accessor
    @Mutable
    public void setId(class_2960 var1);

    @Accessor
    @Mutable
    public void setWidth(int var1);

    @Accessor
    @Mutable
    public void setHeight(int var1);

    @Accessor
    @Mutable
    public void setImage(class_1011 var1);

    @Accessor
    @Mutable
    public void setMipmapLevelsImages(class_1011[] var1);

    @Accessor
    @Mutable
    public void setAnimation(class_7764.class_5790 var1);
}

