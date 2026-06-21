/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.class_1058
 *  net.minecraft.class_2960
 *  net.minecraft.class_7766$class_7767
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.collection.IntIntList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.taski.builtin.StepTask;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_1058;
import net.minecraft.class_2960;
import net.minecraft.class_7766;

public final class DashStitchResult {
    public final int width;
    public final int height;
    public final int mipLevel;
    public final int missing;
    public final IntIntList regions;

    public DashStitchResult(int width, int height, int mipLevel, int missing, IntIntList regions) {
        this.width = width;
        this.height = height;
        this.mipLevel = mipLevel;
        this.missing = missing;
        this.regions = regions;
    }

    public DashStitchResult(class_7766.class_7767 stitchResult, RegistryWriter writer, StepTask task) {
        this.width = stitchResult.comp_1040();
        this.height = stitchResult.comp_1041();
        this.mipLevel = stitchResult.comp_1042();
        this.missing = writer.add(stitchResult.comp_1043());
        this.regions = new IntIntList();
        stitchResult.comp_1044().forEach((identifier, sprite) -> {
            this.regions.put(writer.add(identifier), writer.add(sprite));
            task.next();
        });
    }

    public class_7766.class_7767 export(RegistryReader reader) {
        Object2ObjectOpenHashMap regions = new Object2ObjectOpenHashMap();
        this.regions.forEach((arg_0, arg_1) -> DashStitchResult.lambda$export$1((Map)regions, reader, arg_0, arg_1));
        return new class_7766.class_7767(this.width, this.height, this.mipLevel, (class_1058)reader.get(this.missing), (Map)regions, CompletableFuture.runAsync(() -> {
            throw new RuntimeException("Cached object not yet finalized");
        }));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashStitchResult that = (DashStitchResult)o;
        if (this.width != that.width) {
            return false;
        }
        if (this.height != that.height) {
            return false;
        }
        if (this.mipLevel != that.mipLevel) {
            return false;
        }
        if (this.missing != that.missing) {
            return false;
        }
        return this.regions.equals(that.regions);
    }

    public int hashCode() {
        int result = this.width;
        result = 31 * result + this.height;
        result = 31 * result + this.mipLevel;
        result = 31 * result + this.missing;
        result = 31 * result + this.regions.hashCode();
        return result;
    }

    private static /* synthetic */ void lambda$export$1(Map regions, RegistryReader reader, int key, int value) {
        regions.put((class_2960)reader.get(key), (class_1058)reader.get(value));
    }
}

