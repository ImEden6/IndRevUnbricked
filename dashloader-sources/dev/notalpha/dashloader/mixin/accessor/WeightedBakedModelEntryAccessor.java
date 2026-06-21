/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_6007
 *  net.minecraft.class_6008$class_6010
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_6007;
import net.minecraft.class_6008;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_6008.class_6010.class})
public interface WeightedBakedModelEntryAccessor {
    @Invoker(value="<init>")
    public static class_6008.class_6010 init(Object data, class_6007 weight) {
        throw new AssertionError();
    }
}

