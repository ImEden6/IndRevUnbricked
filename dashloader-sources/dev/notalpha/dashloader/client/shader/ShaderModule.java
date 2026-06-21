/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.class_5944
 */
package dev.notalpha.dashloader.client.shader;

import dev.notalpha.dashloader.api.CachingData;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.collection.ObjectIntList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.config.Option;
import dev.notalpha.taski.builtin.StepTask;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_5944;

public class ShaderModule
implements DashModule<Data> {
    public static final CachingData<HashMap<String, class_5944>> SHADERS = new CachingData();
    public static final CachingData<Int2ObjectMap<List<String>>> WRITE_PROGRAM_SOURCES = new CachingData(CacheStatus.SAVE);

    @Override
    public void reset(Cache cache) {
        SHADERS.reset(cache, new HashMap());
        WRITE_PROGRAM_SOURCES.reset(cache, (Int2ObjectMap<List<String>>)new Int2ObjectOpenHashMap());
    }

    @Override
    public Data save(RegistryWriter factory, StepTask task) {
        Map minecraftData = SHADERS.get(CacheStatus.SAVE);
        if (minecraftData == null) {
            return null;
        }
        ObjectIntList<String> shaders = new ObjectIntList<String>();
        task.doForEach(minecraftData, (s, shader) -> shaders.put((String)s, factory.add(shader)));
        return new Data(shaders);
    }

    @Override
    public void load(Data data, RegistryReader reader, StepTask task) {
        HashMap out = new HashMap();
        data.shaders.forEach((key, value) -> out.put(key, (class_5944)reader.get(value)));
        SHADERS.set(CacheStatus.LOAD, out);
    }

    @Override
    public Class<Data> getDataClass() {
        return Data.class;
    }

    @Override
    public boolean isActive() {
        return ConfigHandler.optionActive(Option.CACHE_SHADER);
    }

    public static final class Data {
        public final ObjectIntList<String> shaders;

        public Data(ObjectIntList<String> shaders) {
            this.shaders = shaders;
        }
    }
}

