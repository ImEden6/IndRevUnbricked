/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.registry;

public class RegistryAddException
extends RuntimeException {
    public final Class<?> targetClass;
    public final Object object;

    public RegistryAddException(Class<?> targetClass, Object object) {
        this.targetClass = targetClass;
        this.object = object;
    }

    @Override
    public String getMessage() {
        return "Could not find a ChunkWriter for " + this.targetClass + ": " + this.object;
    }
}

