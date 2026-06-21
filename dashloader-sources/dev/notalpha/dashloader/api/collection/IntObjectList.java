/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.collection;

import java.util.ArrayList;
import java.util.List;

public record IntObjectList<V>(List<IntObjectEntry<V>> list) {
    public IntObjectList() {
        this(new ArrayList<IntObjectEntry<V>>());
    }

    public void put(int key, V value) {
        this.list.add(new IntObjectEntry<V>(key, value));
    }

    public void forEach(IntObjectConsumer<V> c) {
        this.list.forEach((? super T v) -> c.accept(v.key, v.value));
    }

    public record IntObjectEntry<V>(int key, V value) {
    }

    @FunctionalInterface
    public static interface IntObjectConsumer<V> {
        public void accept(int var1, V var2);
    }
}

