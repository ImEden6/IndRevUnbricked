/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7764$class_5791
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_7764;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_7764.class_5791.class})
public interface SpriteAnimationFrameAccessor {
    @Invoker(value="<init>")
    public static class_7764.class_5791 newSpriteFrame(int index, int time) {
        throw new AssertionError();
    }

    @Accessor
    public int getIndex();

    @Accessor
    public int getTime();
}

