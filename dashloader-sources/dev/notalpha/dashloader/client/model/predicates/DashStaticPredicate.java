/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.client.model.predicates;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.client.model.predicates.BooleanSelector;

public final class DashStaticPredicate
implements DashObject<BooleanSelector> {
    public final boolean value;

    public DashStaticPredicate(boolean value) {
        this.value = value;
    }

    public DashStaticPredicate(BooleanSelector multipartModelSelector) {
        this.value = multipartModelSelector.selector;
    }

    @Override
    public BooleanSelector export(RegistryReader exportHandler) {
        return new BooleanSelector(this.value);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashStaticPredicate that = (DashStaticPredicate)o;
        return this.value == that.value;
    }

    public int hashCode() {
        return this.value ? 1 : 0;
    }
}

