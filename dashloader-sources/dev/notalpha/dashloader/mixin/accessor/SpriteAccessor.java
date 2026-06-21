/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2960
 *  net.minecraft.class_7764
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1058;
import net.minecraft.class_2960;
import net.minecraft.class_7764;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1058.class})
public interface SpriteAccessor {
    @Invoker(value="<init>")
    public static class_1058 init(class_2960 atlasId, class_7764 contents, int atlasWidth, int atlasHeight, int width, int height) {
        throw new AssertionError();
    }
}

