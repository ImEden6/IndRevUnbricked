/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_806
 *  net.minecraft.class_806$class_5827
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.DashModelOverrideListBakedOverride;
import dev.notalpha.dashloader.mixin.accessor.ModelOverrideListAccessor;
import java.util.Arrays;
import net.minecraft.class_2960;
import net.minecraft.class_806;

public final class DashModelOverrideList {
    public final DashModelOverrideListBakedOverride[] overrides;
    public final int[] conditionTypes;

    public DashModelOverrideList(DashModelOverrideListBakedOverride[] overrides, int[] conditionTypes) {
        this.overrides = overrides;
        this.conditionTypes = conditionTypes;
    }

    public DashModelOverrideList(class_806 modelOverrideList, RegistryWriter writer) {
        int i;
        class_806.class_5827[] overrides = ((ModelOverrideListAccessor)modelOverrideList).getOverrides();
        class_2960[] conditionTypes = ((ModelOverrideListAccessor)modelOverrideList).getConditionTypes();
        this.overrides = new DashModelOverrideListBakedOverride[overrides.length];
        this.conditionTypes = new int[conditionTypes.length];
        for (i = 0; i < overrides.length; ++i) {
            this.overrides[i] = new DashModelOverrideListBakedOverride(overrides[i], writer);
        }
        for (i = 0; i < conditionTypes.length; ++i) {
            this.conditionTypes[i] = writer.add(conditionTypes[i]);
        }
    }

    public class_806 export(RegistryReader reader) {
        class_806 out = ModelOverrideListAccessor.newModelOverrideList();
        ModelOverrideListAccessor access = (ModelOverrideListAccessor)out;
        class_2960[] conditionTypesOut = new class_2960[this.conditionTypes.length];
        for (int i = 0; i < this.conditionTypes.length; ++i) {
            conditionTypesOut[i] = (class_2960)reader.get(this.conditionTypes[i]);
        }
        class_806.class_5827[] overridesOut = new class_806.class_5827[this.overrides.length];
        for (int i = 0; i < this.overrides.length; ++i) {
            overridesOut[i] = this.overrides[i].export(reader);
        }
        access.setConditionTypes(conditionTypesOut);
        access.setOverrides(overridesOut);
        return out;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashModelOverrideList that = (DashModelOverrideList)o;
        if (!Arrays.equals(this.overrides, that.overrides)) {
            return false;
        }
        return Arrays.equals(this.conditionTypes, that.conditionTypes);
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.overrides);
        result = 31 * result + Arrays.hashCode(this.conditionTypes);
        return result;
    }
}

