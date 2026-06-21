/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.api;

import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CachingData<D> {
    @Nullable
    private D data = null;
    private Cache cacheManager;
    @Nullable
    private CacheStatus dataStatus;
    @Nullable
    private final CacheStatus onlyOn;

    public CachingData(@Nullable CacheStatus onlyOn) {
        this.onlyOn = onlyOn;
    }

    public CachingData() {
        this(null);
    }

    public void visit(CacheStatus status, Consumer<D> consumer) {
        if (this.active(status)) {
            consumer.accept(this.data);
        }
    }

    @Nullable
    public D get(CacheStatus status) {
        if (this.active(status)) {
            return this.data;
        }
        return null;
    }

    public void reset(Cache cacheManager, @NotNull D data) {
        this.cacheManager = cacheManager;
        this.set(cacheManager.getStatus(), data);
    }

    public void set(CacheStatus status, @NotNull D data) {
        if (this.onlyOn != null && this.onlyOn != status) {
            this.data = null;
            this.dataStatus = null;
            return;
        }
        if (this.cacheManager == null) {
            throw new RuntimeException("cacheManager is null. This OptionData has never been reset in its handler.");
        }
        CacheStatus currentStatus = this.cacheManager.getStatus();
        if (status == currentStatus) {
            this.dataStatus = status;
            this.data = data;
        }
    }

    public boolean active(CacheStatus status) {
        return status == this.dataStatus && status == this.cacheManager.getStatus() && this.data != null && (this.onlyOn == null || this.onlyOn == status);
    }
}

