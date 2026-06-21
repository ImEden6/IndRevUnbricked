/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_812
 *  net.minecraft.class_815
 */
package dev.notalpha.dashloader.client.model.predicates;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.AndMultipartModelSelectorAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.class_812;
import net.minecraft.class_815;

public final class DashAndPredicate
implements DashObject<class_812> {
    public final int[] selectors;

    public DashAndPredicate(int[] selectors) {
        this.selectors = selectors;
    }

    public DashAndPredicate(class_812 selector, RegistryWriter writer) {
        AndMultipartModelSelectorAccessor access = (AndMultipartModelSelectorAccessor)selector;
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
    public class_812 export(RegistryReader handler) {
        ArrayList<class_815> selectors = new ArrayList<class_815>(this.selectors.length);
        for (int accessSelector : this.selectors) {
            selectors.add((class_815)handler.get(accessSelector));
        }
        return new class_812(selectors);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashAndPredicate that = (DashAndPredicate)o;
        return Arrays.equals(this.selectors, that.selectors);
    }

    public int hashCode() {
        return Arrays.hashCode(this.selectors);
    }
}

