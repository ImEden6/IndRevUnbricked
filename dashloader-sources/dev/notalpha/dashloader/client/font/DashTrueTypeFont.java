/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArraySet
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3298
 *  net.minecraft.class_395
 *  org.lwjgl.stb.STBTTFontinfo
 *  org.lwjgl.stb.STBTruetype
 *  org.lwjgl.system.MemoryUtil
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.client.font.FontModule;
import dev.notalpha.dashloader.io.IOHelper;
import dev.notalpha.dashloader.misc.UnsafeHelper;
import dev.notalpha.dashloader.mixin.accessor.TrueTypeFontAccessor;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_395;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

public final class DashTrueTypeFont
implements DashObject<class_395> {
    public final byte[] ttfBuffer;
    public final float oversample;
    public final List<Integer> excludedCharacters;
    public final float shiftX;
    public final float shiftY;
    public final float scaleFactor;
    public final float ascent;

    public DashTrueTypeFont(byte[] ttfBuffer, float oversample, List<Integer> excludedCharacters, float shiftX, float shiftY, float scaleFactor, float ascent) {
        this.ttfBuffer = ttfBuffer;
        this.oversample = oversample;
        this.excludedCharacters = excludedCharacters;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.scaleFactor = scaleFactor;
        this.ascent = ascent;
    }

    public DashTrueTypeFont(class_395 font) {
        TrueTypeFontAccessor fontAccess = (TrueTypeFontAccessor)font;
        class_2960 ttFont = FontModule.FONT_TO_IDENT.get(CacheStatus.SAVE).get(fontAccess.getInfo());
        byte[] data = null;
        try {
            Optional resource = class_310.method_1551().method_1478().method_14486(new class_2960(ttFont.method_12836(), "font/" + ttFont.method_12832()));
            if (resource.isPresent()) {
                data = IOHelper.streamToArray(((class_3298)resource.get()).method_14482());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.ttfBuffer = data;
        this.oversample = fontAccess.getOversample();
        this.excludedCharacters = new ArrayList<Integer>((Collection<Integer>)fontAccess.getExcludedCharacters());
        this.shiftX = fontAccess.getShiftX();
        this.shiftY = fontAccess.getShiftY();
        this.scaleFactor = fontAccess.getScaleFactor();
        this.ascent = fontAccess.getAscent();
    }

    @Override
    public class_395 export(RegistryReader handler) {
        STBTTFontinfo sTBTTFontinfo = STBTTFontinfo.malloc();
        ByteBuffer byteBuffer2 = MemoryUtil.memAlloc((int)this.ttfBuffer.length);
        byteBuffer2.put(this.ttfBuffer);
        byteBuffer2.flip();
        if (!STBTruetype.stbtt_InitFont((STBTTFontinfo)sTBTTFontinfo, (ByteBuffer)byteBuffer2)) {
            try {
                throw new IOException("Invalid ttf");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        class_395 trueTypeFont = UnsafeHelper.allocateInstance(class_395.class);
        TrueTypeFontAccessor trueTypeFontAccess = (TrueTypeFontAccessor)trueTypeFont;
        trueTypeFontAccess.setInfo(sTBTTFontinfo);
        trueTypeFontAccess.setOversample(this.oversample);
        trueTypeFontAccess.setBuffer(byteBuffer2);
        trueTypeFontAccess.setExcludedCharacters((IntSet)new IntArraySet(this.excludedCharacters));
        trueTypeFontAccess.setShiftX(this.shiftX);
        trueTypeFontAccess.setShiftY(this.shiftY);
        trueTypeFontAccess.setScaleFactor(this.scaleFactor);
        trueTypeFontAccess.setAscent(this.ascent);
        return trueTypeFont;
    }
}

