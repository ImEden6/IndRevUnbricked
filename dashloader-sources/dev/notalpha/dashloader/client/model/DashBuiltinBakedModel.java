/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_1058
 *  net.minecraft.class_1090
 *  net.minecraft.class_809
 */
package dev.notalpha.dashloader.client.model;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.components.DashModelOverrideList;
import dev.notalpha.dashloader.client.model.components.DashModelTransformation;
import dev.notalpha.dashloader.mixin.accessor.BuiltinBakedModelAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.Objects;
import net.minecraft.class_1058;
import net.minecraft.class_1090;
import net.minecraft.class_809;

public final class DashBuiltinBakedModel
implements DashObject<class_1090> {
    public final @DataNullable DashModelTransformation transformation;
    public final DashModelOverrideList itemPropertyOverrides;
    public final int spritePointer;
    public final boolean sideLit;

    public DashBuiltinBakedModel(DashModelTransformation transformation, DashModelOverrideList itemPropertyOverrides, int spritePointer, boolean sideLit) {
        this.transformation = transformation;
        this.itemPropertyOverrides = itemPropertyOverrides;
        this.spritePointer = spritePointer;
        this.sideLit = sideLit;
    }

    public DashBuiltinBakedModel(class_1090 builtinBakedModel, RegistryWriter writer) {
        BuiltinBakedModelAccessor access = (BuiltinBakedModelAccessor)builtinBakedModel;
        class_809 transformation = access.getTransformation();
        this.transformation = DashModelTransformation.createDashOrReturnNullIfDefault(transformation);
        this.itemPropertyOverrides = new DashModelOverrideList(access.getItemPropertyOverrides(), writer);
        this.spritePointer = writer.add(access.getSprite());
        this.sideLit = access.getSideLit();
    }

    @Override
    public class_1090 export(RegistryReader reader) {
        class_1058 sprite = (class_1058)reader.get(this.spritePointer);
        return new class_1090(DashModelTransformation.exportOrDefault(this.transformation), this.itemPropertyOverrides.export(reader), sprite, this.sideLit);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashBuiltinBakedModel that = (DashBuiltinBakedModel)o;
        if (this.spritePointer != that.spritePointer) {
            return false;
        }
        if (this.sideLit != that.sideLit) {
            return false;
        }
        if (!Objects.equals(this.transformation, that.transformation)) {
            return false;
        }
        return this.itemPropertyOverrides.equals(that.itemPropertyOverrides);
    }

    public int hashCode() {
        int result = this.transformation != null ? this.transformation.hashCode() : 0;
        result = 31 * result + this.itemPropertyOverrides.hashCode();
        result = 31 * result + this.spritePointer;
        result = 31 * result + (this.sideLit ? 1 : 0);
        return result;
    }
}

