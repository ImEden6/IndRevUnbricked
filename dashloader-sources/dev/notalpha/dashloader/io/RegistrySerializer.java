/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.StepTask
 *  dev.quantumfusion.hyphen.io.ByteBufferIO
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 */
package dev.notalpha.dashloader.io;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.DashObjectClass;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.io.IOHelper;
import dev.notalpha.dashloader.io.Serializer;
import dev.notalpha.dashloader.io.data.CacheInfo;
import dev.notalpha.dashloader.io.data.ChunkInfo;
import dev.notalpha.dashloader.io.data.fragment.CacheFragment;
import dev.notalpha.dashloader.io.data.fragment.ChunkFragment;
import dev.notalpha.dashloader.io.data.fragment.StageFragment;
import dev.notalpha.dashloader.io.fragment.Fragment;
import dev.notalpha.dashloader.io.fragment.Piece;
import dev.notalpha.dashloader.io.fragment.SimplePiece;
import dev.notalpha.dashloader.io.fragment.SizePiece;
import dev.notalpha.dashloader.registry.RegistryWriterImpl;
import dev.notalpha.dashloader.registry.data.ChunkData;
import dev.notalpha.dashloader.registry.data.ChunkFactory;
import dev.notalpha.dashloader.registry.data.StageData;
import dev.notalpha.dashloader.thread.ThreadHandler;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.StepTask;
import dev.quantumfusion.hyphen.io.ByteBufferIO;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RegistrySerializer {
    private static final int MIN_PER_THREAD_FRAGMENT_SIZE = 0x1400000;
    private static final int MAX_FRAGMENT_SIZE = 0x40000000;
    private final Object2ObjectMap<Class<?>, Serializer<?>> serializers;
    private final int compressionLevel;

    public RegistrySerializer(List<DashObjectClass<?, ?>> dashObjects) {
        this.compressionLevel = ConfigHandler.INSTANCE.config.compression;
        this.serializers = new Object2ObjectOpenHashMap();
        for (DashObjectClass<?, ?> dashObject : dashObjects) {
            Class<?> dashClass = dashObject.getDashClass();
            this.serializers.put(dashClass, new Serializer(dashClass));
        }
    }

    public <D extends DashObject<?>> Serializer<D> getSerializer(DashObjectClass<?, D> dashObject) {
        return (Serializer)this.serializers.get(dashObject.getDashClass());
    }

    public CacheInfo serialize(Path dir, RegistryWriterImpl factory, Consumer<Task> taskConsumer) throws IOException {
        StageData[] stages = factory.export();
        Piece[] value = new SimplePiece[stages.length];
        for (int i = 0; i < stages.length; ++i) {
            StageData stage = stages[i];
            Piece[] value2 = new SimplePiece[stage.chunks.length];
            for (int i1 = 0; i1 < stage.chunks.length; ++i1) {
                ChunkData<?, ?> chunk = stage.chunks[i1];
                Serializer<?> serializer = this.getSerializer(chunk.dashObject);
                Piece[] value3 = new SizePiece[chunk.dashables.length];
                for (int i2 = 0; i2 < chunk.dashables.length; ++i2) {
                    value3[i2] = new SizePiece(serializer.measure(chunk.dashables[i2].data) + 4L);
                }
                value2[i1] = new SimplePiece(value3);
            }
            value[i] = new SimplePiece(value2);
        }
        SimplePiece piece = new SimplePiece(value);
        int[][] stageSizes = new int[stages.length][];
        for (int i = 0; i < stages.length; ++i) {
            StageData stage = stages[i];
            int[] chunkSizes = new int[stage.chunks.length];
            for (int i1 = 0; i1 < stage.chunks.length; ++i1) {
                chunkSizes[i1] = stage.chunks[i1].dashables.length;
            }
            stageSizes[i] = chunkSizes;
        }
        int minFragments = (int)(piece.size / 0x40000000L);
        int maxFragments = (int)(piece.size / 0x1400000L);
        int fragmentCount = Integer.max(Integer.max(Integer.min(ThreadHandler.THREADS, maxFragments), minFragments), 1);
        long remainingSize = piece.size;
        ArrayList<CacheFragment> fragments = new ArrayList<CacheFragment>();
        for (int i = 0; i < fragmentCount; ++i) {
            long fragmentSize = remainingSize / (long)(fragmentCount - i);
            if (i == fragmentCount - 1) {
                fragmentSize = Long.MAX_VALUE;
            }
            Fragment fragment = piece.fragment(fragmentSize);
            remainingSize -= fragment.size;
            fragments.add(new CacheFragment(fragment));
        }
        StepTask task = new StepTask("fragment", fragments.size() * 2);
        taskConsumer.accept((Task)task);
        for (int k = 0; k < fragments.size(); ++k) {
            DashLoader.LOG.info("Serializing fragment " + k);
            CacheFragment fragment = (CacheFragment)fragments.get(k);
            List<StageFragment> stageFragmentMetadata = fragment.stages;
            ByteBufferIO io = ByteBufferIO.createDirect((int)((int)fragment.info.fileSize));
            int taskSize = 0;
            for (StageFragment stage : stageFragmentMetadata) {
                for (ChunkFragment chunk : stage.chunks) {
                    taskSize += chunk.info.rangeEnd - chunk.info.rangeStart;
                }
            }
            StepTask stageTask = new StepTask("stage", taskSize);
            task.setSubTask((Task)stageTask);
            for (int i = 0; i < stageFragmentMetadata.size(); ++i) {
                StageFragment stage = stageFragmentMetadata.get(i);
                StageData data = stages[i + fragment.info.rangeStart];
                List<ChunkFragment> chunks = stage.chunks;
                for (int j = 0; j < chunks.size(); ++j) {
                    ChunkFragment chunk = chunks.get(j);
                    ChunkData<?, ?> chunkData = data.chunks[j + stage.info.rangeStart];
                    Serializer serializer = (Serializer)this.serializers.get(chunkData.dashObject.getDashClass());
                    for (int i1 = chunk.info.rangeStart; i1 < chunk.info.rangeEnd; ++i1) {
                        ChunkData.Entry dashable = chunkData.dashables[i1];
                        io.putInt(dashable.pos);
                        serializer.put(io, dashable.data);
                        stageTask.next();
                    }
                }
            }
            task.next();
            StepTask serializingTask = new StepTask("Serializing");
            task.setSubTask((Task)serializingTask);
            int fileSize = (int)fragment.info.fileSize;
            IOHelper.save(this.fragmentFilePath(dir, k), serializingTask, io, fileSize, (byte)this.compressionLevel);
            task.next();
        }
        ArrayList<ChunkInfo> chunks = new ArrayList<ChunkInfo>();
        for (ChunkFactory<?, ?> chunk : factory.chunks) {
            chunks.add(new ChunkInfo(chunk));
        }
        return new CacheInfo(fragments, chunks, stageSizes);
    }

    public StageData[] deserialize(Path dir, CacheInfo metadata, List<DashObjectClass<?, ?>> objects) {
        StageData[] out = new StageData[metadata.stageSizes.length];
        for (int i = 0; i < metadata.stageSizes.length; ++i) {
            int[] chunkSizes = metadata.stageSizes[i];
            ChunkData[] chunks = new ChunkData[chunkSizes.length];
            for (int j = 0; j < chunks.length; ++j) {
                ChunkInfo chunkInfo = metadata.chunks.get(j);
                chunks[j] = new ChunkData((byte)j, chunkInfo.name, objects.get(chunkInfo.dashObjectId), new ChunkData.Entry[chunkSizes[j]]);
            }
            out[i] = new StageData(chunks);
        }
        List<CacheFragment> fragments = metadata.fragments;
        ArrayList<Runnable> runnables = new ArrayList<Runnable>();
        int j = 0;
        while (j < fragments.size()) {
            CacheFragment fragment = fragments.get(j);
            int finalJ = j++;
            runnables.add(() -> {
                try {
                    ByteBufferIO io = IOHelper.load(this.fragmentFilePath(dir, finalJ));
                    this.deserialize(out, io, fragment);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (ConfigHandler.INSTANCE.config.singleThreadedReading) {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
        } else {
            ThreadHandler.INSTANCE.parallelRunnable(runnables);
        }
        return out;
    }

    private void deserialize(StageData[] data, ByteBufferIO io, CacheFragment fragment) {
        for (int i = 0; i < fragment.stages.size(); ++i) {
            StageFragment stageFragment = fragment.stages.get(i);
            StageData stage = data[fragment.info.rangeStart + i];
            for (int i1 = 0; i1 < stageFragment.chunks.size(); ++i1) {
                ChunkFragment chunkFragment = stageFragment.chunks.get(i1);
                ChunkData<?, ?> chunkData = stage.chunks[stageFragment.info.rangeStart + i1];
                Serializer<?> serializer = this.getSerializer(chunkData.dashObject);
                for (int i2 = chunkFragment.info.rangeStart; i2 < chunkFragment.info.rangeEnd; ++i2) {
                    int pos = io.getInt();
                    Object out = serializer.get(io);
                    chunkData.dashables[i2] = new ChunkData.Entry(out, pos);
                }
            }
        }
    }

    private Path fragmentFilePath(Path dir, int fragment) {
        return dir.resolve("fragment-" + fragment + ".bin");
    }
}

