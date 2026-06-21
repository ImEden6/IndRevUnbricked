/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_6007
 *  net.minecraft.class_6008$class_6010
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.WeightedBakedModelEntryAccessor;
import net.minecraft.class_1087;
import net.minecraft.class_6007;
import net.minecraft.class_6008;

public final class DashWeightedModelEntry {
    public final int model;
    public final int weight;

    public DashWeightedModelEntry(int model, int weight) {
        this.model = model;
        this.weight = weight;
    }

    public DashWeightedModelEntry(class_6008.class_6010<class_1087> entry, RegistryWriter writer) {
        this(writer.add((class_1087)entry.method_34983()), entry.method_34979().method_34976());
    }

    public class_6008.class_6010<class_1087> export(RegistryReader handler) {
        return WeightedBakedModelEntryAccessor.init(handler.get(this.model), class_6007.method_34977((int)this.weight));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashWeightedModelEntry that = (DashWeightedModelEntry)o;
        if (this.model != that.model) {
            return false;
        }
        return this.weight == that.weight;
    }

    public int hashCode() {
        int result = this.model;
        result = 31 * result + this.weight;
        return result;
    }
}

