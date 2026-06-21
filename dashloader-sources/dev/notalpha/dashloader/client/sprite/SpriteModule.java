/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.StepTask
 *  net.minecraft.class_2960
 *  net.minecraft.class_7766$class_7767
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.CachingData;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.collection.IntObjectList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.sprite.DashStitchResult;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.config.Option;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.StepTask;
import java.util.HashMap;
import net.minecraft.class_2960;
import net.minecraft.class_7766;

public class SpriteModule
implements DashModule<Data> {
    public static final CachingData<HashMap<class_2960, class_7766.class_7767>> ATLASES = new CachingData();
    public static final CachingData<HashMap<class_2960, class_2960>> ATLAS_IDS = new CachingData(CacheStatus.SAVE);

    @Override
    public void reset(Cache cache) {
        ATLASES.reset(cache, new HashMap());
        ATLAS_IDS.reset(cache, new HashMap());
    }

    @Override
    public Data save(RegistryWriter factory, StepTask task) {
        IntObjectList<DashStitchResult> results = new IntObjectList<DashStitchResult>();
        HashMap<class_2960, class_7766.class_7767> map = ATLASES.get(CacheStatus.SAVE);
        task.doForEach(map, (identifier, stitchResult) -> {
            StepTask atlases = new StepTask("atlas", stitchResult.comp_1044().size());
            task.setSubTask((Task)atlases);
            results.put(factory.add(identifier), new DashStitchResult((class_7766.class_7767)stitchResult, factory, atlases));
        });
        return new Data(results);
    }

    @Override
    public void load(Data data, RegistryReader reader, StepTask task) {
        HashMap stitchResults = new HashMap(data.results.list().size());
        data.results.forEach((identifier, stitchResult) -> stitchResults.put((class_2960)reader.get(identifier), stitchResult.export(reader)));
        ATLASES.set(CacheStatus.LOAD, stitchResults);
    }

    @Override
    public Class<Data> getDataClass() {
        return Data.class;
    }

    @Override
    public boolean isActive() {
        return ConfigHandler.optionActive(Option.CACHE_SPRITES);
    }

    public static final class Data {
        public final IntObjectList<DashStitchResult> results;

        public Data(IntObjectList<DashStitchResult> results) {
            this.results = results;
        }
    }
}

