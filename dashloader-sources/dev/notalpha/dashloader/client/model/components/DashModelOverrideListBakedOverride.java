/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_1087
 *  net.minecraft.class_806$class_5827
 *  net.minecraft.class_806$class_5828
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.DashModelOverrideListInlinedCondition;
import dev.notalpha.dashloader.mixin.accessor.ModelOverrideListBakedOverrideAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.class_1087;
import net.minecraft.class_806;
import org.jetbrains.annotations.Nullable;

public final class DashModelOverrideListBakedOverride {
    public final DashModelOverrideListInlinedCondition[] conditions;
    public final @DataNullable Integer model;

    public DashModelOverrideListBakedOverride(DashModelOverrideListInlinedCondition[] conditions, @Nullable Integer model) {
        this.conditions = conditions;
        this.model = model;
    }

    public DashModelOverrideListBakedOverride(class_806.class_5827 override, RegistryWriter writer) {
        class_806.class_5828[] conditionsIn = ((ModelOverrideListBakedOverrideAccessor)override).getConditions();
        class_1087 bakedModel = ((ModelOverrideListBakedOverrideAccessor)override).getModel();
        this.model = bakedModel == null ? null : Integer.valueOf(writer.add(bakedModel));
        this.conditions = new DashModelOverrideListInlinedCondition[conditionsIn.length];
        for (int i = 0; i < conditionsIn.length; ++i) {
            this.conditions[i] = new DashModelOverrideListInlinedCondition(conditionsIn[i]);
        }
    }

    public class_806.class_5827 export(RegistryReader reader) {
        class_806.class_5828[] conditionsOut = new class_806.class_5828[this.conditions.length];
        for (int i = 0; i < this.conditions.length; ++i) {
            conditionsOut[i] = this.conditions[i].export();
        }
        return ModelOverrideListBakedOverrideAccessor.newModelOverrideListBakedOverride(conditionsOut, this.model == null ? null : (class_1087)reader.get(this.model));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashModelOverrideListBakedOverride that = (DashModelOverrideListBakedOverride)o;
        if (!Arrays.equals(this.conditions, that.conditions)) {
            return false;
        }
        return Objects.equals(this.model, that.model);
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.conditions);
        result = 31 * result + (this.model != null ? this.model.hashCode() : 0);
        return result;
    }
}

