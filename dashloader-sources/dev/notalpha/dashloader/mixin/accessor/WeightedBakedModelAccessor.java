/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1097
 *  net.minecraft.class_6008$class_6010
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.List;
import net.minecraft.class_1087;
import net.minecraft.class_1097;
import net.minecraft.class_6008;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1097.class})
public interface WeightedBakedModelAccessor {
    @Accessor(value="models")
    public List<class_6008.class_6010<class_1087>> getBakedModels();
}

