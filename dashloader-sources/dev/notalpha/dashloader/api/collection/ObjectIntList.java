/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.collection;

import java.util.ArrayList;
import java.util.List;

public record ObjectIntList<K>(List<ObjectIntEntry<K>> list) {
    public ObjectIntList() {
        this(new ArrayList<ObjectIntEntry<K>>());
    }

    public void put(K key, int value) {
        this.list.add(new ObjectIntEntry<K>(key, value));
    }

    public void forEach(ObjectIntConsumer<K> c) {
        this.list.forEach((? super T v) -> c.accept(v.key, v.value));
    }

    public record ObjectIntEntry<K>(K key, int value) {
    }

    @FunctionalInterface
    public static interface ObjectIntConsumer<K> {
        public void accept(K var1, int var2);
    }
}

