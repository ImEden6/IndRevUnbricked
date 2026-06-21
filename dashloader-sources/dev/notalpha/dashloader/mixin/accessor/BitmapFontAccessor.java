/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_386
 *  net.minecraft.class_386$class_388
 *  net.minecraft.class_8532
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1011;
import net.minecraft.class_386;
import net.minecraft.class_8532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_386.class})
public interface BitmapFontAccessor {
    @Invoker(value="<init>")
    public static class_386 init(class_1011 image, class_8532<class_386.class_388> glyphs) {
        throw new AssertionError();
    }

    @Accessor
    public class_8532<class_386.class_388> getGlyphs();

    @Accessor
    public class_1011 getImage();
}

