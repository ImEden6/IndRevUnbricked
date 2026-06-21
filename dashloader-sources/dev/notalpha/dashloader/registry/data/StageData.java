/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.registry.data;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.registry.data.ChunkData;

public class StageData {
    public final ChunkData<?, ?>[] chunks;

    public StageData(ChunkData<?, ?>[] chunks) {
        this.chunks = chunks;
    }

    public void preExport(RegistryReader reader) {
        for (ChunkData<?, ?> chunk : this.chunks) {
            chunk.preExport(reader);
        }
    }

    public void export(Object[][] data, RegistryReader registry) {
        for (int i = 0; i < this.chunks.length; ++i) {
            ChunkData<?, ?> chunk = this.chunks[i];
            chunk.export(data[i], registry);
        }
    }

    public void postExport(RegistryReader reader) {
        for (ChunkData<?, ?> chunk : this.chunks) {
            chunk.postExport(reader);
        }
    }
}

