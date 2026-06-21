/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1091
 *  net.minecraft.class_2960$class_7658
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1091;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1091.class})
public interface ModelIdentifierAccessor {
    @Invoker(value="<init>")
    public static class_1091 init(String namespace, String path, String variant, @Nullable class_2960.class_7658 extraData) {
        throw new AssertionError();
    }
}

