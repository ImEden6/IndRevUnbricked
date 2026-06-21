/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2FloatArrayMap
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  net.minecraft.class_379
 *  net.minecraft.class_7166
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import it.unimi.dsi.fastutil.ints.Int2FloatArrayMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Map;
import net.minecraft.class_379;
import net.minecraft.class_7166;

public final class DashSpaceFont
implements DashObject<class_7166> {
    public final int[] ints;
    public final float[] floats;

    public DashSpaceFont(int[] ints, float[] floats) {
        this.ints = ints;
        this.floats = floats;
    }

    public DashSpaceFont(class_7166 font) {
        IntSet glyphs = font.method_27442();
        this.ints = new int[glyphs.size()];
        this.floats = new float[glyphs.size()];
        int i = 0;
        for (Integer providedGlyph : glyphs) {
            class_379 glyph = font.method_2040(providedGlyph.intValue());
            assert (glyph != null);
            this.ints[i] = providedGlyph;
            this.floats[i] = glyph.getAdvance();
            ++i;
        }
    }

    @Override
    public class_7166 export(RegistryReader exportHandler) {
        return new class_7166((Map)new Int2FloatArrayMap(this.ints, this.floats));
    }
}

