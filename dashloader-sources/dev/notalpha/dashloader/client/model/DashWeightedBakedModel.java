/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1097
 *  net.minecraft.class_6008$class_6010
 */
package dev.notalpha.dashloader.client.model;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.DashWeightedModelEntry;
import dev.notalpha.dashloader.mixin.accessor.WeightedBakedModelAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1087;
import net.minecraft.class_1097;
import net.minecraft.class_6008;

public final class DashWeightedBakedModel
implements DashObject<class_1097> {
    public final List<DashWeightedModelEntry> models;

    public DashWeightedBakedModel(List<DashWeightedModelEntry> models) {
        this.models = models;
    }

    public DashWeightedBakedModel(class_1097 model, RegistryWriter writer) {
        this.models = new ArrayList<DashWeightedModelEntry>();
        for (class_6008.class_6010<class_1087> weightedModel : ((WeightedBakedModelAccessor)model).getBakedModels()) {
            this.models.add(new DashWeightedModelEntry(weightedModel, writer));
        }
    }

    @Override
    public class_1097 export(RegistryReader reader) {
        ArrayList<class_6008.class_6010<class_1087>> modelsOut = new ArrayList<class_6008.class_6010<class_1087>>();
        for (DashWeightedModelEntry model : this.models) {
            modelsOut.add(model.export(reader));
        }
        return new class_1097(modelsOut);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashWeightedBakedModel that = (DashWeightedBakedModel)o;
        return this.models.equals(that.models);
    }

    public int hashCode() {
        return this.models.hashCode();
    }
}

