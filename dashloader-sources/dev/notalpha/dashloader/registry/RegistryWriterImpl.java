/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ByteMap
 *  it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap
 */
package dev.notalpha.dashloader.registry;

import dev.notalpha.dashloader.DashObjectClass;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryAddException;
import dev.notalpha.dashloader.api.registry.RegistryUtil;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.registry.FactoryBinding;
import dev.notalpha.dashloader.registry.MissingHandler;
import dev.notalpha.dashloader.registry.TrackingRegistryWriterImpl;
import dev.notalpha.dashloader.registry.data.ChunkData;
import dev.notalpha.dashloader.registry.data.ChunkFactory;
import dev.notalpha.dashloader.registry.data.StageData;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public final class RegistryWriterImpl
implements RegistryWriter {
    private final IdentityHashMap<?, Integer> dedup = new IdentityHashMap();
    private final Object2ByteMap<Class<?>> target2chunkMappings = new Object2ByteOpenHashMap();
    private final Object2ByteMap<Class<?>> dash2chunkMappings;
    private final List<MissingHandler<?>> missingHandlers;
    public final ChunkFactory<?, ?>[] chunks;

    private RegistryWriterImpl(ChunkFactory<?, ?>[] chunks, List<MissingHandler<?>> missingHandlers) {
        this.target2chunkMappings.defaultReturnValue((byte)-1);
        this.dash2chunkMappings = new Object2ByteOpenHashMap();
        this.dash2chunkMappings.defaultReturnValue((byte)-1);
        this.missingHandlers = missingHandlers;
        this.chunks = chunks;
    }

    public static <R, D extends DashObject<R>> RegistryWriterImpl create(List<MissingHandler<?>> missingHandlers, List<DashObjectClass<?, ?>> dashObjects) {
        if (dashObjects.size() > 63) {
            throw new RuntimeException("Hit group limit of 63. Please contact notalpha if you hit this limit!");
        }
        ChunkFactory[] chunks = new ChunkFactory[dashObjects.size()];
        RegistryWriterImpl writer = new RegistryWriterImpl(chunks, missingHandlers);
        for (int i = 0; i < dashObjects.size(); ++i) {
            DashObjectClass<?, ?> dashObject = dashObjects.get(i);
            Class<?> dashClass = dashObject.getDashClass();
            Class<?> targetClass = dashObject.getTargetClass();
            byte old = writer.target2chunkMappings.put(targetClass, (byte)i);
            if (old != -1) {
                DashObjectClass<?, ?> conflicting = dashObjects.get(old);
                throw new IllegalStateException("DashObjects \"" + dashObject.getDashClass() + "\" and \"" + conflicting.getDashClass() + "\" have the same target class \"" + targetClass + "\".");
            }
            writer.dash2chunkMappings.put(dashClass, (byte)i);
            FactoryBinding<?, ?> factory = FactoryBinding.create(dashObject);
            String name = dashClass.getSimpleName();
            chunks[i] = new ChunkFactory((byte)i, name, factory, dashObject);
        }
        return writer;
    }

    @Override
    public <R> int add(R object) {
        return this.addObject(object);
    }

    private <R, D extends DashObject<R>> int addObject(R object) {
        ChunkFactory.Entry<DashObject> entry;
        if (this.dedup.containsKey(object)) {
            return this.dedup.get(object);
        }
        if (object == null) {
            throw new NullPointerException("Registry add argument is null");
        }
        Class<?> targetClass = object.getClass();
        Integer pointer = null;
        byte chunkPos = this.target2chunkMappings.getByte(targetClass);
        if (chunkPos != -1) {
            ChunkFactory<?, ?> chunk = this.chunks[chunkPos];
            entry = TrackingRegistryWriterImpl.create(this, (RegistryWriter writer) -> chunk.create(object, (RegistryWriter)writer));
            pointer = chunk.add(entry, this);
        }
        if (pointer == null) {
            for (MissingHandler<?> missingHandler : this.missingHandlers) {
                if (!missingHandler.parentClass.isAssignableFrom(targetClass)) continue;
                entry = TrackingRegistryWriterImpl.create(this, (RegistryWriter writer) -> missingHandler.func.apply(object, (RegistryWriter)writer));
                if (entry.data == null) continue;
                Class<?> dashClass = ((DashObject)entry.data).getClass();
                byte chunkPos2 = this.dash2chunkMappings.getByte(dashClass);
                if (chunkPos2 == -1) {
                    throw new RuntimeException("Could not find a ChunkWriter for DashClass " + dashClass);
                }
                ChunkFactory<?, ?> chunk = this.chunks[chunkPos2];
                pointer = chunk.add(entry, this);
                break;
            }
        }
        if (pointer == null) {
            throw new RegistryAddException(targetClass, object);
        }
        this.dedup.put(object, pointer);
        return pointer;
    }

    public <D> ChunkFactory.Entry<D> get(int id) {
        return this.chunks[RegistryUtil.getChunkId((int)id)].list.get(RegistryUtil.getObjectId(id));
    }

    public StageData[] export() {
        ArrayDeque exposedQueue = new ArrayDeque();
        for (ChunkFactory<?, ?> chunk : this.chunks) {
            for (ChunkFactory.Entry entry : chunk.list) {
                if (entry.references != 0) continue;
                entry.stage = 0;
                exposedQueue.offer(entry);
            }
        }
        int stages = 1;
        while (!exposedQueue.isEmpty()) {
            ChunkFactory.Entry element = (ChunkFactory.Entry)exposedQueue.poll();
            for (int dependencyId : element.dependencies) {
                ChunkFactory.Entry dependency = this.get(dependencyId);
                if (dependency.stage <= element.stage) {
                    dependency.stage = element.stage + 1;
                    if (dependency.stage >= stages) {
                        stages = dependency.stage + 1;
                    }
                }
                if (--dependency.references != 0) continue;
                exposedQueue.offer(dependency);
            }
        }
        StageData[] out = new StageData[stages];
        for (int i = 0; i < stages; ++i) {
            ChunkData[] chunksOut = new ChunkData[this.chunks.length];
            for (int j = 0; j < this.chunks.length; ++j) {
                ChunkFactory<?, ?> chunk = this.chunks[j];
                ArrayList dashablesOut = new ArrayList();
                for (int k = 0; k < chunk.list.size(); ++k) {
                    ChunkFactory.Entry entry = chunk.list.get(k);
                    if (entry.stage != i) continue;
                    dashablesOut.add(new ChunkData.Entry(entry.data, k));
                }
                chunksOut[j] = new ChunkData(chunk.chunkId, chunk.name, chunk.dashObject, (ChunkData.Entry[])dashablesOut.toArray(ChunkData.Entry[]::new));
            }
            out[stages - (i + 1)] = new StageData(chunksOut);
        }
        return out;
    }
}

