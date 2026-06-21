/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.class_2960
 *  net.minecraft.class_378
 *  net.minecraft.class_390
 *  org.apache.commons.lang3.tuple.Pair
 *  org.lwjgl.stb.STBTTFontinfo
 */
package dev.notalpha.dashloader.client.font;

import dev.notalpha.dashloader.api.CachingData;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.collection.IntObjectList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.config.Option;
import dev.notalpha.taski.builtin.StepTask;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_2960;
import net.minecraft.class_378;
import net.minecraft.class_390;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.stb.STBTTFontinfo;

public class FontModule
implements DashModule<Data> {
    public static class_378 FONTMANAGER;
    public static final CachingData<Object2ObjectMap<class_2960, Pair<Int2ObjectMap<IntList>, List<class_390>>>> DATA;
    public static final CachingData<Map<STBTTFontinfo, class_2960>> FONT_TO_IDENT;

    @Override
    public void reset(Cache cache) {
        DATA.reset(cache, (Object2ObjectMap<class_2960, Pair<Int2ObjectMap<IntList>, List<class_390>>>)new Object2ObjectOpenHashMap());
        FONT_TO_IDENT.reset(cache, new HashMap());
    }

    @Override
    public Data save(RegistryWriter factory, StepTask task) {
        IntObjectList<DashFontStorage> fontMap = new IntObjectList<DashFontStorage>();
        Object2ObjectMap<class_2960, Pair<Int2ObjectMap<IntList>, List<class_390>>> identifierPairObject2ObjectMap = DATA.get(CacheStatus.SAVE);
        identifierPairObject2ObjectMap.forEach((identifier, fontList) -> {
            ArrayList<Integer> fontsOut = new ArrayList<Integer>();
            for (class_390 font : (List)fontList.getValue()) {
                fontsOut.add(factory.add(font));
            }
            IntObjectList<List<Integer>> charactersByWidth = new IntObjectList<List<Integer>>();
            ((Int2ObjectMap)fontList.getKey()).forEach(charactersByWidth::put);
            fontMap.put(factory.add(identifier), new DashFontStorage(charactersByWidth, fontsOut));
            task.next();
        });
        return new Data(fontMap);
    }

    @Override
    public void load(Data data, RegistryReader reader, StepTask task) {
        Object2ObjectOpenHashMap out = new Object2ObjectOpenHashMap();
        data.fontMap.forEach((arg_0, arg_1) -> FontModule.lambda$load$3(reader, (Object2ObjectMap)out, arg_0, arg_1));
        DATA.set(CacheStatus.LOAD, (Object2ObjectMap<class_2960, Pair<Int2ObjectMap<IntList>, List<class_390>>>)out);
    }

    @Override
    public Class<Data> getDataClass() {
        return Data.class;
    }

    @Override
    public boolean isActive() {
        return ConfigHandler.optionActive(Option.CACHE_FONT);
    }

    private static /* synthetic */ void lambda$load$3(RegistryReader reader, Object2ObjectMap out, int key, DashFontStorage value) {
        ArrayList fontsOut = new ArrayList();
        value.fonts.forEach(fontPointer -> fontsOut.add((class_390)reader.get((int)fontPointer)));
        Int2ObjectOpenHashMap charactersByWidth = new Int2ObjectOpenHashMap();
        value.charactersByWidth.forEach((arg_0, arg_1) -> FontModule.lambda$load$2((Int2ObjectMap)charactersByWidth, arg_0, arg_1));
        out.put((Object)((class_2960)reader.get(key)), (Object)Pair.of((Object)charactersByWidth, fontsOut));
    }

    private static /* synthetic */ void lambda$load$2(Int2ObjectMap charactersByWidth, int key1, List value1) {
        charactersByWidth.put(key1, (Object)new IntArrayList((Collection)value1));
    }

    static {
        DATA = new CachingData();
        FONT_TO_IDENT = new CachingData();
    }

    public static final class Data {
        public final IntObjectList<DashFontStorage> fontMap;

        public Data(IntObjectList<DashFontStorage> fontMap) {
            this.fontMap = fontMap;
        }
    }

    public static final class DashFontStorage {
        public final IntObjectList<List<Integer>> charactersByWidth;
        public final List<Integer> fonts;

        public DashFontStorage(IntObjectList<List<Integer>> charactersByWidth, List<Integer> fonts) {
            this.charactersByWidth = charactersByWidth;
            this.fonts = fonts;
        }
    }
}

