/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1090
 *  net.minecraft.class_806
 *  net.minecraft.class_809
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1058;
import net.minecraft.class_1090;
import net.minecraft.class_806;
import net.minecraft.class_809;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1090.class})
public interface BuiltinBakedModelAccessor {
    @Accessor
    public class_809 getTransformation();

    @Accessor
    public class_806 getItemPropertyOverrides();

    @Accessor
    public class_1058 getSprite();

    @Accessor
    public boolean getSideLit();
}

