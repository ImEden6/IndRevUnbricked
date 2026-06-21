/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_815
 *  net.minecraft.class_821
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import net.minecraft.class_815;
import net.minecraft.class_821;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_821.class})
public interface OrMultipartModelSelectorAccessor {
    @Accessor
    public Iterable<? extends class_815> getSelectors();
}

