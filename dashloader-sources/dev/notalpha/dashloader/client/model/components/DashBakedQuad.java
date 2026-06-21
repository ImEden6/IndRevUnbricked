/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2350
 *  net.minecraft.class_777
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import java.util.Arrays;
import net.minecraft.class_1058;
import net.minecraft.class_2350;
import net.minecraft.class_777;

public final class DashBakedQuad
implements DashObject<class_777> {
    public final int[] vertexData;
    public final int colorIndex;
    public final class_2350 face;
    public final boolean shade;
    public final int sprite;

    public DashBakedQuad(int[] vertexData, int colorIndex, class_2350 face, boolean shade, int sprite) {
        this.vertexData = vertexData;
        this.colorIndex = colorIndex;
        this.face = face;
        this.shade = shade;
        this.sprite = sprite;
    }

    public DashBakedQuad(class_777 bakedQuad, RegistryWriter writer) {
        this(bakedQuad.method_3357(), bakedQuad.method_3359(), bakedQuad.method_3358(), bakedQuad.method_24874(), writer.add(bakedQuad.method_35788()));
    }

    @Override
    public class_777 export(RegistryReader handler) {
        return new class_777(this.vertexData, this.colorIndex, this.face, (class_1058)handler.get(this.sprite), this.shade);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashBakedQuad that = (DashBakedQuad)o;
        if (this.colorIndex != that.colorIndex) {
            return false;
        }
        if (this.shade != that.shade) {
            return false;
        }
        if (this.sprite != that.sprite) {
            return false;
        }
        if (!Arrays.equals(this.vertexData, that.vertexData)) {
            return false;
        }
        return this.face == that.face;
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.vertexData);
        result = 31 * result + this.colorIndex;
        result = 31 * result + this.face.hashCode();
        result = 31 * result + (this.shade ? 1 : 0);
        result = 31 * result + this.sprite;
        return result;
    }
}

