/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 */
package dev.notalpha.dashloader.registry;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.registry.RegistryWriterImpl;
import dev.notalpha.dashloader.registry.data.ChunkFactory;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.function.Function;

public final class TrackingRegistryWriterImpl
implements RegistryWriter {
    private final RegistryWriterImpl factory;
    private final IntList dependencies = new IntArrayList();

    private TrackingRegistryWriterImpl(RegistryWriterImpl factory) {
        this.factory = factory;
    }

    @Override
    public <R> int add(R object) {
        int value = this.factory.add(object);
        this.dependencies.add(value);
        return value;
    }

    static <R, D extends DashObject<R>> ChunkFactory.Entry<D> create(RegistryWriterImpl factory, Function<RegistryWriter, D> function) {
        TrackingRegistryWriterImpl writer = new TrackingRegistryWriterImpl(factory);
        DashObject data = (DashObject)function.apply(writer);
        int[] dependencies = writer.dependencies.toIntArray();
        return new ChunkFactory.Entry<DashObject>(data, dependencies);
    }
}

