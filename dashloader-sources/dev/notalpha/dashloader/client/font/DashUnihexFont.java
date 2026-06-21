/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_391
 *  net.minecraft.class_391$class_393
 *  net.minecraft.class_391$class_8544
 *  net.minecraft.class_8532
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.collection.IntObjectList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.UnihexFontAccessor;
import net.minecraft.class_391;
import net.minecraft.class_8532;

public final class DashUnihexFont
implements DashObject<class_391> {
    public final IntObjectList<class_391.class_393> glyphs;

    public DashUnihexFont(IntObjectList<class_391.class_393> glyphs) {
        this.glyphs = glyphs;
    }

    public DashUnihexFont(class_391 rawFont, RegistryWriter writer) {
        this.glyphs = new IntObjectList();
        UnihexFontAccessor font = (UnihexFontAccessor)rawFont;
        class_8532<class_391.class_393> fontImages = font.getGlyphs();
        fontImages.method_51601(this.glyphs::put);
    }

    @Override
    public class_391 export(RegistryReader handler) {
        class_8532 container = new class_8532(class_391.class_393[]::new, x$0 -> new class_391.class_393[x$0][]);
        this.glyphs.forEach((arg_0, arg_1) -> ((class_8532)container).method_51599(arg_0, arg_1));
        return UnihexFontAccessor.create((class_8532<class_391.class_393>)container);
    }

    public static class DashUnicodeTextureGlyph {
        public final class_391.class_8544 contents;
        public final int left;
        public final int right;

        public DashUnicodeTextureGlyph(class_391.class_8544 contents, int left, int right) {
            this.contents = contents;
            this.left = left;
            this.right = right;
        }

        public DashUnicodeTextureGlyph(class_391.class_393 glyph) {
            this.contents = glyph.comp_1508();
            this.left = glyph.comp_1509();
            this.right = glyph.comp_1510();
        }
    }
}

