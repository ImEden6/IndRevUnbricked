/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1088$class_7776
 *  net.minecraft.class_2960
 *  net.minecraft.class_4590
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package dev.notalpha.dashloader.mixin.option.misc;

import net.minecraft.class_1088;
import net.minecraft.class_2960;
import net.minecraft.class_4590;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_1088.class_7776.class}, priority=999)
public class ModelLoaderBakedModelCacheKeyMixin {
    @Shadow
    @Final
    private class_2960 comp_1053;
    @Shadow
    @Final
    private boolean comp_1055;
    @Shadow
    @Final
    private class_4590 comp_1054;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelLoaderBakedModelCacheKeyMixin)) {
            return false;
        }
        ModelLoaderBakedModelCacheKeyMixin that = (ModelLoaderBakedModelCacheKeyMixin)o;
        if (this.comp_1055 != that.comp_1055) {
            return false;
        }
        if (!this.comp_1053.equals((Object)that.comp_1053)) {
            return false;
        }
        return this.comp_1054.equals((Object)that.comp_1054);
    }

    public int hashCode() {
        int result = this.comp_1053.hashCode();
        result = 31 * result + this.comp_1054.hashCode();
        result = 31 * result + (this.comp_1055 ? 1 : 0);
        return result;
    }
}

