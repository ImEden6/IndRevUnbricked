/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.StepTask
 *  dev.notalpha.taski.builtin.WeightedStageTask
 *  dev.notalpha.taski.builtin.WeightedStageTask$WeightedStage
 *  dev.quantumfusion.hyphen.io.ByteBufferIO
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 */
package dev.notalpha.dashloader.io;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.io.IOHelper;
import dev.notalpha.dashloader.io.Serializer;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.StepTask;
import dev.notalpha.taski.builtin.WeightedStageTask;
import dev.quantumfusion.hyphen.io.ByteBufferIO;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MappingSerializer {
    private final Object2ObjectMap<Class<?>, Serializer<?>> serializers = new Object2ObjectOpenHashMap();

    public MappingSerializer(List<DashModule<?>> cacheHandlers) {
        cacheHandlers.forEach(handler -> {
            Class dataClass = handler.getDataClass();
            this.serializers.put(dataClass, new Serializer(dataClass));
        });
    }

    public void save(Path dir, RegistryWriter factory, List<DashModule<?>> handlers, StepTask parent) {
        Serializer serializer;
        ArrayList<WeightedStageTask.WeightedStage> tasks = new ArrayList<WeightedStageTask.WeightedStage>();
        for (DashModule<?> value : handlers) {
            tasks.add(new WeightedStageTask.WeightedStage(value.taskWeight(), (Task)new StepTask(value.getDataClass().getSimpleName(), 1)));
        }
        WeightedStageTask stageTask = new WeightedStageTask("Mapping", tasks);
        parent.setSubTask((Task)stageTask);
        ArrayList<Object> objects = new ArrayList<Object>();
        int i = 0;
        for (DashModule<?> handler : handlers) {
            Task task = ((WeightedStageTask.WeightedStage)stageTask.getStages().get((int)i)).task;
            if (handler.isActive()) {
                Object object = handler.save(factory, (StepTask)task);
                Class<?> clazz = handler.getDataClass();
                if (object.getClass() != clazz) {
                    throw new RuntimeException("Handler DataClass does not match the output of saveMappings on " + handler.getClass());
                }
                objects.add(object);
            } else {
                objects.add(null);
            }
            task.finish();
            ++i;
        }
        Path path = dir.resolve("mapping.bin");
        int measure = 0;
        for (Object object : objects) {
            ++measure;
            if (object == null) continue;
            Class<?> clazz = object.getClass();
            serializer = (Serializer)this.serializers.get(clazz);
            if (serializer == null) {
                throw new RuntimeException("Could not find mapping serializer for " + clazz);
            }
            measure = (int)((long)measure + serializer.measure(object));
        }
        ByteBufferIO io = ByteBufferIO.createDirect((int)measure);
        for (Object e : objects) {
            if (e == null) {
                io.putByte((byte)0);
                continue;
            }
            io.putByte((byte)1);
            serializer = (Serializer)this.serializers.get(e.getClass());
            serializer.put(io, e);
        }
        try {
            io.rewind();
            IOHelper.save(path, new StepTask(""), io, measure, ConfigHandler.INSTANCE.config.compression);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean load(Path dir, RegistryReader reader, List<DashModule<?>> handlers) {
        try {
            ByteBufferIO io = IOHelper.load(dir.resolve("mapping.bin"));
            for (DashModule<?> handler : handlers) {
                if (io.getByte() == 0 && handler.isActive()) {
                    DashLoader.LOG.info("Recaching as " + handler.getClass().getSimpleName() + " is now active.");
                    return false;
                }
                Class<?> dataClass = handler.getDataClass();
                Serializer serializer = (Serializer)this.serializers.get(dataClass);
                Object object = serializer.get(io);
                if (!handler.isActive()) continue;
                handler.load(object, reader, new StepTask(""));
            }
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

