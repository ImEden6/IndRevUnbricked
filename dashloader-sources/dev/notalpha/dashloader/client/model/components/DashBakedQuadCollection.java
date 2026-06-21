/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_777
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.BakedQuadCollection;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_777;

public class DashBakedQuadCollection
implements DashObject<BakedQuadCollection> {
    public final List<Integer> quads;

    public DashBakedQuadCollection(List<Integer> quads) {
        this.quads = quads;
    }

    public DashBakedQuadCollection(BakedQuadCollection quads, RegistryWriter writer) {
        this.quads = new ArrayList<Integer>();
        for (class_777 quad : quads.quads) {
            this.quads.add(writer.add(quad));
        }
    }

    @Override
    public BakedQuadCollection export(RegistryReader reader) {
        ArrayList<class_777> out = new ArrayList<class_777>(this.quads.size());
        for (Integer quad : this.quads) {
            out.add((class_777)reader.get(quad));
        }
        return new BakedQuadCollection(out);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashBakedQuadCollection that = (DashBakedQuadCollection)o;
        return this.quads.equals(that.quads);
    }

    public int hashCode() {
        return this.quads.hashCode();
    }
}

