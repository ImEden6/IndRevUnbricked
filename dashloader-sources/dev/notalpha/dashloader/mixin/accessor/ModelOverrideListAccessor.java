/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_806
 *  net.minecraft.class_806$class_5827
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_2960;
import net.minecraft.class_806;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_806.class})
public interface ModelOverrideListAccessor {
    @Invoker(value="<init>")
    public static class_806 newModelOverrideList() {
        throw new AssertionError();
    }

    @Accessor
    public class_806.class_5827[] getOverrides();

    @Accessor
    @Mutable
    public void setOverrides(class_806.class_5827[] var1);

    @Accessor
    public class_2960[] getConditionTypes();

    @Accessor
    @Mutable
    public void setConditionTypes(class_2960[] var1);
}

