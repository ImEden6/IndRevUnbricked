/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.api.cache;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.taski.builtin.StepTask;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface Cache {
    public void load(String var1);

    public boolean save(@Nullable Consumer<StepTask> var1);

    public void reset();

    public void remove();

    public CacheStatus getStatus();

    public Path getDir();
}

