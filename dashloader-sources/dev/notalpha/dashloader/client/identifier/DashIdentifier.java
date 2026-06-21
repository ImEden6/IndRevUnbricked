/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 */
package dev.notalpha.dashloader.client.identifier;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.mixin.accessor.IdentifierAccessor;
import net.minecraft.class_2960;

public final class DashIdentifier
implements DashObject<class_2960> {
    public final String namespace;
    public final String path;

    public DashIdentifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public DashIdentifier(class_2960 identifier) {
        this.namespace = identifier.method_12836();
        this.path = identifier.method_12832();
    }

    @Override
    public class_2960 export(RegistryReader exportHandler) {
        return IdentifierAccessor.init(this.namespace, this.path, null);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashIdentifier that = (DashIdentifier)o;
        if (!this.namespace.equals(that.namespace)) {
            return false;
        }
        return this.path.equals(that.path);
    }

    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.path.hashCode();
        return result;
    }
}

