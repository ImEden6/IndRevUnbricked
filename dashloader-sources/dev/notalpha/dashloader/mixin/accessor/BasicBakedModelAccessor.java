/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1093
 *  net.minecraft.class_2350
 *  net.minecraft.class_777
 *  net.minecraft.class_806
 *  net.minecraft.class_809
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.List;
import java.util.Map;
import net.minecraft.class_1058;
import net.minecraft.class_1093;
import net.minecraft.class_2350;
import net.minecraft.class_777;
import net.minecraft.class_806;
import net.minecraft.class_809;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1093.class})
public interface BasicBakedModelAccessor {
    @Accessor
    public List<class_777> getQuads();

    @Accessor
    public Map<class_2350, List<class_777>> getFaceQuads();

    @Accessor
    public boolean getUsesAo();

    @Accessor
    public boolean getHasDepth();

    @Accessor
    public boolean getIsSideLit();

    @Accessor
    public class_1058 getSprite();

    @Accessor
    public class_809 getTransformation();

    @Accessor
    public class_806 getItemPropertyOverrides();
}

