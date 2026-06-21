/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.io.fragment;

import java.util.List;

public class Fragment {
    public final long size;
    public final int startIndex;
    public final int endIndex;
    public final List<Fragment> inner;

    public Fragment(long size, int startIndex, int endIndex, List<Fragment> inner) {
        this.size = size;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.inner = inner;
    }

    public String toString() {
        return "Fragment{size=" + this.size + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", inner=" + this.inner + "}";
    }
}

