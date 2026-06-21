/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_815
 *  net.minecraft.class_821
 */
package dev.notalpha.dashloader.client.model.predicates;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.OrMultipartModelSelectorAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.class_815;
import net.minecraft.class_821;

public final class DashOrPredicate
implements DashObject<class_821> {
    public final int[] selectors;

    public DashOrPredicate(int[] selectors) {
        this.selectors = selectors;
    }

    public DashOrPredicate(class_821 selector, RegistryWriter writer) {
        OrMultipartModelSelectorAccessor access = (OrMultipartModelSelectorAccessor)selector;
        Iterable<? extends class_815> accessSelectors = access.getSelectors();
        int count = 0;
        for (class_815 class_8152 : accessSelectors) {
            ++count;
        }
        this.selectors = new int[count];
        int i = 0;
        for (class_815 class_8153 : accessSelectors) {
            this.selectors[i++] = writer.add(class_8153);
        }
    }

    @Override
    public class_821 export(RegistryReader handler) {
        ArrayList<class_815> selectors = new ArrayList<class_815>(this.selectors.length);
        for (int accessSelector : this.selectors) {
            selectors.add((class_815)handler.get(accessSelector));
        }
        return new class_821(selectors);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashOrPredicate that = (DashOrPredicate)o;
        return Arrays.equals(this.selectors, that.selectors);
    }

    public int hashCode() {
        return Arrays.hashCode(this.selectors);
    }
}

