/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1091
 */
package dev.notalpha.dashloader.client.identifier;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.mixin.accessor.ModelIdentifierAccessor;
import net.minecraft.class_1091;

public final class DashModelIdentifier
implements DashObject<class_1091> {
    public final String namespace;
    public final String path;
    public final String variant;

    public DashModelIdentifier(class_1091 identifier) {
        this.namespace = identifier.method_12836();
        this.path = identifier.method_12832();
        this.variant = identifier.method_4740();
    }

    public DashModelIdentifier(String namespace, String path, String variant) {
        this.namespace = namespace;
        this.path = path;
        this.variant = variant;
    }

    @Override
    public class_1091 export(RegistryReader exportHandler) {
        return ModelIdentifierAccessor.init(this.namespace, this.path, this.variant, null);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashModelIdentifier that = (DashModelIdentifier)o;
        if (!this.namespace.equals(that.namespace)) {
            return false;
        }
        if (!this.path.equals(that.path)) {
            return false;
        }
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.path.hashCode();
        result = 31 * result + this.variant.hashCode();
        return result;
    }
}

