/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_1011$class_1012
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1011;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1011.class})
public interface NativeImageAccessor {
    @Invoker(value="<init>")
    public static class_1011 init(class_1011.class_1012 format, int width, int height, boolean useStb, long pointer) {
        throw new AssertionError();
    }

    @Accessor
    public long getPointer();

    @Accessor
    public boolean getIsStbImage();
}

