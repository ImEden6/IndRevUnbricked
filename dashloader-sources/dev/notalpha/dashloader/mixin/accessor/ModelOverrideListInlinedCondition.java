/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_806$class_5828
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_806;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_806.class_5828.class})
public interface ModelOverrideListInlinedCondition {
    @Invoker(value="<init>")
    public static class_806.class_5828 newModelOverrideListInlinedCondition(int index, float threshold) {
        throw new AssertionError();
    }
}

