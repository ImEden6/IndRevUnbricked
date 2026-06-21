/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_806$class_5828
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.mixin.accessor.ModelOverrideListInlinedCondition;
import net.minecraft.class_806;

public final class DashModelOverrideListInlinedCondition {
    public final int index;
    public final float threshold;

    public DashModelOverrideListInlinedCondition(int index, float threshold) {
        this.index = index;
        this.threshold = threshold;
    }

    public DashModelOverrideListInlinedCondition(class_806.class_5828 inlinedCondition) {
        this(inlinedCondition.field_28796, inlinedCondition.field_28797);
    }

    public class_806.class_5828 export() {
        return ModelOverrideListInlinedCondition.newModelOverrideListInlinedCondition(this.index, this.threshold);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashModelOverrideListInlinedCondition that = (DashModelOverrideListInlinedCondition)o;
        if (this.index != that.index) {
            return false;
        }
        return Float.compare(that.threshold, this.threshold) == 0;
    }

    public int hashCode() {
        int result = this.index;
        result = 31 * result + (this.threshold != 0.0f ? Float.floatToIntBits(this.threshold) : 0);
        return result;
    }
}

