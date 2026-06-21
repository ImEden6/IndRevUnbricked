/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_386$class_388
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.BitmapFontGlyphAccessor;
import net.minecraft.class_1011;
import net.minecraft.class_386;

public final class DashBitmapFontGlyph {
    public final float scaleFactor;
    public final int image;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int advance;
    public final int ascent;

    public DashBitmapFontGlyph(float scaleFactor, int image, int x, int y, int width, int height, int advance, int ascent) {
        this.scaleFactor = scaleFactor;
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.advance = advance;
        this.ascent = ascent;
    }

    public DashBitmapFontGlyph(class_386.class_388 bitmapFontGlyph, RegistryWriter writer) {
        BitmapFontGlyphAccessor font = (BitmapFontGlyphAccessor)bitmapFontGlyph;
        this.scaleFactor = font.getScaleFactor();
        this.image = writer.add(font.getImage());
        this.x = font.getX();
        this.y = font.getY();
        this.width = font.getWidth();
        this.height = font.getHeight();
        this.advance = font.getAdvance();
        this.ascent = font.getAscent();
    }

    public class_386.class_388 export(RegistryReader handler) {
        return BitmapFontGlyphAccessor.init(this.scaleFactor, (class_1011)handler.get(this.image), this.x, this.y, this.width, this.height, this.advance, this.ascent);
    }
}

