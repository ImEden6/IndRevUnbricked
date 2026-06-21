/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_386$class_388
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_1011;
import net.minecraft.class_386;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_386.class_388.class})
public interface BitmapFontGlyphAccessor {
    @Invoker(value="<init>")
    public static class_386.class_388 init(float scaleFactor, class_1011 image, int x, int y, int width, int height, int advance, int ascent) {
        throw new AssertionError();
    }

    @Accessor
    public class_1011 getImage();

    @Accessor(value="x")
    public int getX();

    @Accessor(value="y")
    public int getY();

    @Accessor
    public float getScaleFactor();

    @Accessor
    public int getWidth();

    @Accessor
    public int getHeight();

    @Accessor
    public int getAdvance();

    @Accessor
    public int getAscent();
}

