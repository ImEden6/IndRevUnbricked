/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.cache;

import dev.notalpha.dashloader.CacheFactoryImpl;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import java.nio.file.Path;
import java.util.function.BiFunction;

public interface CacheFactory {
    public static CacheFactory create() {
        return new CacheFactoryImpl();
    }

    public void addDashObject(Class<? extends DashObject<?>> var1);

    public void addModule(DashModule<?> var1);

    public <R> void addMissingHandler(Class<R> var1, BiFunction<R, RegistryWriter, DashObject<? extends R>> var2);

    public Cache build(Path var1);
}

