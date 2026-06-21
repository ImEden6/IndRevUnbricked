/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_1058
 *  net.minecraft.class_1093
 *  net.minecraft.class_2350
 *  net.minecraft.class_5819
 *  net.minecraft.class_777
 */
package dev.notalpha.dashloader.client.model;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.collection.ObjectObjectList;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.BakedQuadCollection;
import dev.notalpha.dashloader.client.model.components.DashModelOverrideList;
import dev.notalpha.dashloader.client.model.components.DashModelTransformation;
import dev.notalpha.dashloader.mixin.accessor.BasicBakedModelAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1058;
import net.minecraft.class_1093;
import net.minecraft.class_2350;
import net.minecraft.class_5819;
import net.minecraft.class_777;

public final class DashBasicBakedModel
implements DashObject<class_1093> {
    public final int quads;
    public final ObjectObjectList<class_2350, Integer> faceQuads;
    public final boolean usesAo;
    public final boolean hasDepth;
    public final boolean isSideLit;
    public final @DataNullable DashModelTransformation transformation;
    public final DashModelOverrideList itemPropertyOverrides;
    public final int spritePointer;

    public DashBasicBakedModel(int quads, ObjectObjectList<class_2350, Integer> faceQuads, boolean usesAo, boolean hasDepth, boolean isSideLit, DashModelTransformation transformation, DashModelOverrideList itemPropertyOverrides, int spritePointer) {
        this.quads = quads;
        this.faceQuads = faceQuads;
        this.usesAo = usesAo;
        this.hasDepth = hasDepth;
        this.isSideLit = isSideLit;
        this.transformation = transformation;
        this.itemPropertyOverrides = itemPropertyOverrides;
        this.spritePointer = spritePointer;
    }

    public DashBasicBakedModel(class_1093 basicBakedModel, RegistryWriter writer) {
        BasicBakedModelAccessor access = (BasicBakedModelAccessor)basicBakedModel;
        basicBakedModel.method_4707(null, null, class_5819.method_43047());
        this.quads = writer.add(new BakedQuadCollection(access.getQuads()));
        this.faceQuads = new ObjectObjectList();
        access.getFaceQuads().forEach((direction, bakedQuads) -> this.faceQuads.put((class_2350)direction, writer.add(new BakedQuadCollection((List<class_777>)bakedQuads))));
        this.itemPropertyOverrides = new DashModelOverrideList(access.getItemPropertyOverrides(), writer);
        this.usesAo = access.getUsesAo();
        this.hasDepth = access.getHasDepth();
        this.isSideLit = access.getIsSideLit();
        this.transformation = DashModelTransformation.createDashOrReturnNullIfDefault(access.getTransformation());
        this.spritePointer = writer.add(access.getSprite());
    }

    @Override
    public class_1093 export(RegistryReader reader) {
        class_1058 sprite = (class_1058)reader.get(this.spritePointer);
        BakedQuadCollection collection = (BakedQuadCollection)reader.get(this.quads);
        List<class_777> quadsOut = collection.quads;
        HashMap<class_2350, List<class_777>> faceQuadsOut = new HashMap<class_2350, List<class_777>>();
        for (ObjectObjectList.ObjectObjectEntry<class_2350, Integer> entry : this.faceQuads.list()) {
            BakedQuadCollection collectionEntry = (BakedQuadCollection)reader.get(entry.value());
            faceQuadsOut.put(entry.key(), collectionEntry.quads);
        }
        return new class_1093(quadsOut, faceQuadsOut, this.usesAo, this.isSideLit, this.hasDepth, sprite, DashModelTransformation.exportOrDefault(this.transformation), this.itemPropertyOverrides.export(reader));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashBasicBakedModel that = (DashBasicBakedModel)o;
        if (this.quads != that.quads) {
            return false;
        }
        if (this.usesAo != that.usesAo) {
            return false;
        }
        if (this.hasDepth != that.hasDepth) {
            return false;
        }
        if (this.isSideLit != that.isSideLit) {
            return false;
        }
        if (this.spritePointer != that.spritePointer) {
            return false;
        }
        if (!this.faceQuads.equals(that.faceQuads)) {
            return false;
        }
        if (!Objects.equals(this.transformation, that.transformation)) {
            return false;
        }
        return this.itemPropertyOverrides.equals(that.itemPropertyOverrides);
    }

    public int hashCode() {
        int result = this.quads;
        result = 31 * result + this.faceQuads.hashCode();
        result = 31 * result + (this.usesAo ? 1 : 0);
        result = 31 * result + (this.hasDepth ? 1 : 0);
        result = 31 * result + (this.isSideLit ? 1 : 0);
        result = 31 * result + (this.transformation != null ? this.transformation.hashCode() : 0);
        result = 31 * result + this.itemPropertyOverrides.hashCode();
        result = 31 * result + this.spritePointer;
        return result;
    }
}

