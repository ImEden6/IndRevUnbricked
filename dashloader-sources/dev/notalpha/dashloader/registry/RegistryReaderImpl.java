/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.StepTask
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.registry;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.io.data.CacheInfo;
import dev.notalpha.dashloader.registry.data.StageData;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.StepTask;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public final class RegistryReaderImpl
implements RegistryReader {
    private final StageData[] chunkData;
    private final Object[][] data;

    public RegistryReaderImpl(CacheInfo metadata, StageData[] data) {
        this.chunkData = data;
        this.data = new Object[metadata.chunks.size()][];
        for (int i = 0; i < metadata.chunks.size(); ++i) {
            this.data[i] = new Object[metadata.chunks.get((int)i).size];
        }
    }

    public final void export(@Nullable Consumer<Task> taskConsumer) {
        StepTask task = new StepTask("Exporting", Integer.max(this.chunkData.length, 1));
        if (taskConsumer != null) {
            taskConsumer.accept((Task)task);
        }
        for (StageData chunkData : this.chunkData) {
            chunkData.preExport(this);
            chunkData.export(this.data, this);
            chunkData.postExport(this);
        }
    }

    @Override
    public final <R> R get(int pointer) {
        return (R)this.data[pointer & 0x3F][pointer >>> 6];
    }
}

