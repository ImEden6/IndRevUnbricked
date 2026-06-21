/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  net.minecraft.class_1087
 *  net.minecraft.class_1091
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2960
 *  net.minecraft.class_773
 *  net.minecraft.class_7923
 *  net.minecraft.class_815
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.NotNull
 */
package dev.notalpha.dashloader.client.model;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.CachingData;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.collection.IntIntList;
import dev.notalpha.dashloader.api.registry.RegistryAddException;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.fallback.UnbakedBakedModel;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.config.Option;
import dev.notalpha.dashloader.mixin.accessor.ModelLoaderAccessor;
import dev.notalpha.taski.builtin.StepTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1087;
import net.minecraft.class_1091;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2960;
import net.minecraft.class_773;
import net.minecraft.class_7923;
import net.minecraft.class_815;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class ModelModule
implements DashModule<Data> {
    public static final CachingData<HashMap<class_2960, class_1087>> MODELS_SAVE = new CachingData(CacheStatus.SAVE);
    public static final CachingData<HashMap<class_2960, UnbakedBakedModel>> MODELS_LOAD = new CachingData(CacheStatus.LOAD);
    public static final CachingData<HashMap<class_2680, class_2960>> MISSING_READ = new CachingData();
    public static final CachingData<HashMap<class_1087, Pair<List<class_815>, class_2689<class_2248, class_2680>>>> MULTIPART_PREDICATES = new CachingData(CacheStatus.SAVE);

    @Override
    public void reset(Cache cache) {
        MODELS_SAVE.reset(cache, new HashMap());
        MODELS_LOAD.reset(cache, new HashMap());
        MISSING_READ.reset(cache, new HashMap());
        MULTIPART_PREDICATES.reset(cache, new HashMap());
    }

    @Override
    public Data save(RegistryWriter factory, StepTask task) {
        HashMap<class_2960, class_1087> models = MODELS_SAVE.get(CacheStatus.SAVE);
        if (models == null) {
            return null;
        }
        IntIntList outModels = new IntIntList(new ArrayList<IntIntList.IntInt>(models.size()));
        IntIntList missingModels = new IntIntList();
        HashSet out = new HashSet();
        task.doForEach(models, (identifier, bakedModel) -> {
            if (bakedModel != null) {
                try {
                    int add = factory.add(bakedModel);
                    outModels.put(factory.add(identifier), add);
                    out.add(identifier);
                }
                catch (RegistryAddException registryAddException) {
                    // empty catch block
                }
            }
        });
        for (class_2248 block : class_7923.field_41175) {
            block.method_9595().method_11662().forEach(blockState -> {
                class_1091 modelId = class_773.method_3340((class_2680)blockState);
                if (!out.contains(modelId)) {
                    missingModels.put(factory.add(blockState), factory.add(modelId));
                }
            });
        }
        return new Data(outModels, missingModels);
    }

    @Override
    public void load(Data data, RegistryReader reader, StepTask task) {
        HashMap out = new HashMap(data.models.list().size());
        data.models.forEach((key, value) -> {
            class_1087 model = (class_1087)reader.get(value);
            class_2960 identifier = (class_2960)reader.get(key);
            out.put(identifier, new UnbakedBakedModel(model, identifier));
        });
        HashMap missingModelsRead = new HashMap();
        data.missingModels.forEach((blockState, modelId) -> missingModelsRead.put((class_2680)reader.get(blockState), (class_2960)reader.get(modelId)));
        DashLoader.LOG.info("Found {} Missing BlockState Models", (Object)missingModelsRead.size());
        MISSING_READ.set(CacheStatus.LOAD, missingModelsRead);
        MODELS_LOAD.set(CacheStatus.LOAD, out);
    }

    @Override
    public Class<Data> getDataClass() {
        return Data.class;
    }

    @Override
    public float taskWeight() {
        return 1000.0f;
    }

    @Override
    public boolean isActive() {
        return ConfigHandler.optionActive(Option.CACHE_MODEL_LOADER);
    }

    public static class_2689<class_2248, class_2680> getStateManager(class_2960 identifier) {
        class_2689<class_2248, class_2680> staticDef = ModelLoaderAccessor.getStaticDefinitions().get(identifier);
        if (staticDef != null) {
            return staticDef;
        }
        return ((class_2248)class_7923.field_41175.method_10223(identifier)).method_9595();
    }

    @NotNull
    public static class_2960 getStateManagerIdentifier(class_2689<class_2248, class_2680> stateManager) {
        for (Map.Entry<class_2960, class_2689<class_2248, class_2680>> entry : ModelLoaderAccessor.getStaticDefinitions().entrySet()) {
            if (entry.getValue() != stateManager) continue;
            return entry.getKey();
        }
        return class_7923.field_41175.method_10221((Object)((class_2248)stateManager.method_11660()));
    }

    public static final class Data {
        public final IntIntList models;
        public final IntIntList missingModels;

        public Data(IntIntList models, IntIntList missingModels) {
            this.models = models;
            this.missingModels = missingModels;
        }
    }
}

