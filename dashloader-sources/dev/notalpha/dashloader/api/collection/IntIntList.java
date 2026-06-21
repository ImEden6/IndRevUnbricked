/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.collection;

import java.util.ArrayList;
import java.util.List;

public record IntIntList(List<IntInt> list) {
    public IntIntList() {
        this(new ArrayList<IntInt>());
    }

    public void put(int key, int value) {
        this.list.add(new IntInt(key, value));
    }

    public void forEach(IntIntConsumer c) {
        this.list.forEach((? super T v) -> c.accept(v.key, v.value));
    }

    public record IntInt(int key, int value) {
    }

    @FunctionalInterface
    public static interface IntIntConsumer {
        public void accept(int var1, int var2);
    }
}

