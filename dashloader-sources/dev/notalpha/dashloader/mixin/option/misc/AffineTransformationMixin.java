/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_4590
 *  org.joml.Matrix4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package dev.notalpha.dashloader.mixin.option.misc;

import java.util.Objects;
import net.minecraft.class_4590;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_4590.class}, priority=999)
public class AffineTransformationMixin {
    @Shadow
    @Final
    private Matrix4f field_20900;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffineTransformationMixin)) {
            return false;
        }
        AffineTransformationMixin that = (AffineTransformationMixin)o;
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.field_20900, that.field_20900);
    }

    public int hashCode() {
        return this.field_20900.hashCode();
    }
}

