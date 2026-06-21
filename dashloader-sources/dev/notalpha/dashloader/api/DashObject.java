/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api;

import dev.notalpha.dashloader.api.registry.RegistryReader;

public interface DashObject<R> {
    default public void preExport(RegistryReader reader) {
    }

    public R export(RegistryReader var1);

    default public void postExport(RegistryReader reader) {
    }
}

