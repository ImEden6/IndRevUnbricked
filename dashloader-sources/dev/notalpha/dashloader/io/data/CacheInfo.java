/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.io.data;

import dev.notalpha.dashloader.io.data.ChunkInfo;
import dev.notalpha.dashloader.io.data.fragment.CacheFragment;
import java.util.List;

public class CacheInfo {
    public final List<CacheFragment> fragments;
    public final List<ChunkInfo> chunks;
    public final int[][] stageSizes;

    public CacheInfo(List<CacheFragment> fragments, List<ChunkInfo> chunks, int[][] stageSizes) {
        this.fragments = fragments;
        this.chunks = chunks;
        this.stageSizes = stageSizes;
    }
}

