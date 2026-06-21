/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 */
package dev.notalpha.dashloader.api;

import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.taski.builtin.StepTask;

public interface DashModule<D> {
    public void reset(Cache var1);

    public D save(RegistryWriter var1, StepTask var2);

    public void load(D var1, RegistryReader var2, StepTask var3);

    public Class<D> getDataClass();

    default public boolean isActive() {
        return true;
    }

    default public float taskWeight() {
        return 100.0f;
    }
}

