/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1091
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package dev.notalpha.dashloader.mixin.option.misc;

import java.util.Objects;
import net.minecraft.class_1091;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_1091.class}, priority=999)
public abstract class ModelIdentifierMixin {
    @Shadow
    @Final
    private String field_5406;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        class_1091 that = (class_1091)o;
        return Objects.equals(this.field_5406, that.method_4740());
    }

    public int hashCode() {
        return 31 * super.hashCode() + this.field_5406.hashCode();
    }
}

