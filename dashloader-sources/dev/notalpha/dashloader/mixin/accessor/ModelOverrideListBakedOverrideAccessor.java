/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_806$class_5827
 *  net.minecraft.class_806$class_5828
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1087;
import net.minecraft.class_806;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_806.class_5827.class})
public interface ModelOverrideListBakedOverrideAccessor {
    @Invoker(value="<init>")
    public static class_806.class_5827 newModelOverrideListBakedOverride(class_806.class_5828[] conditions, @Nullable class_1087 model) {
        throw new AssertionError();
    }

    @Accessor
    public class_806.class_5828[] getConditions();

    @Accessor
    public class_1087 getModel();
}

