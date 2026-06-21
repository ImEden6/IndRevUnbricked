/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7764
 *  net.minecraft.class_7764$class_5790
 *  net.minecraft.class_7764$class_5791
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.List;
import net.minecraft.class_7764;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_7764.class_5790.class})
public interface SpriteAnimationAccessor {
    @Invoker(value="<init>")
    public static class_7764.class_5790 init(class_7764 parent, List<class_7764.class_5791> frames, int frameCount, boolean interpolation) {
        throw new AssertionError();
    }

    @Accessor
    public List<class_7764.class_5791> getFrames();

    @Accessor
    public int getFrameCount();

    @Accessor
    public boolean getInterpolation();
}

