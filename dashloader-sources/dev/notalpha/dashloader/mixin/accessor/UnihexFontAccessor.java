/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_391
 *  net.minecraft.class_391$class_393
 *  net.minecraft.class_8532
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_391;
import net.minecraft.class_8532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_391.class})
public interface UnihexFontAccessor {
    @Invoker(value="<init>")
    public static class_391 create(class_8532<class_391.class_393> glyphs) {
        throw new AssertionError();
    }

    @Accessor
    public class_8532<class_391.class_393> getGlyphs();

    @Accessor
    @Mutable
    public void setGlyphs(class_8532<class_391.class_393> var1);
}

