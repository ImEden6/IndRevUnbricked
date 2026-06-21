/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_818
 */
package dev.notalpha.dashloader.client.model.predicates;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.mixin.accessor.SimpleMultipartModelSelectorAccessor;
import net.minecraft.class_818;

public final class DashSimplePredicate
implements DashObject<class_818> {
    public final String key;
    public final String valueString;

    public DashSimplePredicate(String key, String valueString) {
        this.key = key;
        this.valueString = valueString;
    }

    public DashSimplePredicate(class_818 simpleMultipartModelSelector) {
        SimpleMultipartModelSelectorAccessor access = (SimpleMultipartModelSelectorAccessor)simpleMultipartModelSelector;
        this.key = access.getKey();
        this.valueString = access.getValueString();
    }

    @Override
    public class_818 export(RegistryReader handler) {
        return new class_818(this.key, this.valueString);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashSimplePredicate that = (DashSimplePredicate)o;
        if (!this.key.equals(that.key)) {
            return false;
        }
        return this.valueString.equals(that.valueString);
    }

    public int hashCode() {
        int result = this.key.hashCode();
        result = 31 * result + this.valueString.hashCode();
        return result;
    }
}

