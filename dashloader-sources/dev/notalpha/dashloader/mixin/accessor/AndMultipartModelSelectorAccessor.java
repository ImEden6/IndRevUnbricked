/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_812
 *  net.minecraft.class_815
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_812;
import net.minecraft.class_815;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_812.class})
public interface AndMultipartModelSelectorAccessor {
    @Accessor
    public Iterable<? extends class_815> getSelectors();
}

