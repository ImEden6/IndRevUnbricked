/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.class_377
 *  net.minecraft.class_377$class_7647
 *  net.minecraft.class_382
 *  net.minecraft.class_383
 *  net.minecraft.class_390
 *  net.minecraft.class_8532
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.notalpha.dashloader.mixin.accessor;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.class_377;
import net.minecraft.class_382;
import net.minecraft.class_383;
import net.minecraft.class_390;
import net.minecraft.class_8532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_377.class})
public interface FontStorageAccessor {
    @Accessor
    public void setBlankGlyphRenderer(class_382 var1);

    @Accessor
    public void setWhiteRectangleGlyphRenderer(class_382 var1);

    @Accessor
    public class_8532<class_382> getGlyphRendererCache();

    @Accessor
    public class_8532<class_377.class_7647> getGlyphCache();

    @Accessor
    public Int2ObjectMap<IntList> getCharactersByWidth();

    @Accessor
    public List<class_390> getFonts();

    @Invoker
    public class_382 callGetGlyphRenderer(class_383 var1);

    @Invoker
    public void callCloseFonts();

    @Invoker
    public void callCloseGlyphAtlases();
}

