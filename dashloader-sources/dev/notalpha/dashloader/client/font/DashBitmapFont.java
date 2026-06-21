/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_386
 *  net.minecraft.class_386$class_388
 *  net.minecraft.class_8532
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.collection.IntObjectList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.font.DashBitmapFontGlyph;
import dev.notalpha.dashloader.mixin.accessor.BitmapFontAccessor;
import java.util.ArrayList;
import net.minecraft.class_1011;
import net.minecraft.class_386;
import net.minecraft.class_8532;

public final class DashBitmapFont
implements DashObject<class_386> {
    public final int image;
    public final IntObjectList<DashBitmapFontGlyph> glyphs;

    public DashBitmapFont(int image, IntObjectList<DashBitmapFontGlyph> glyphs) {
        this.image = image;
        this.glyphs = glyphs;
    }

    public DashBitmapFont(class_386 bitmapFont, RegistryWriter writer) {
        BitmapFontAccessor font = (BitmapFontAccessor)bitmapFont;
        this.image = writer.add(font.getImage());
        this.glyphs = new IntObjectList(new ArrayList());
        font.getGlyphs().method_51601((integer, bitmapFontGlyph) -> this.glyphs.put(integer, new DashBitmapFontGlyph((class_386.class_388)bitmapFontGlyph, writer)));
    }

    @Override
    public class_386 export(RegistryReader reader) {
        class_8532 out = new class_8532(class_386.class_388[]::new, x$0 -> new class_386.class_388[x$0][]);
        this.glyphs.forEach((key, value) -> out.method_51599(key, (Object)value.export(reader)));
        return BitmapFontAccessor.init((class_1011)reader.get(this.image), (class_8532<class_386.class_388>)out);
    }
}

