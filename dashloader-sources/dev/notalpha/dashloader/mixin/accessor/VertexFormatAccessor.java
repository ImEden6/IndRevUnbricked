/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.class_293
 *  net.minecraft.class_296
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import com.google.common.collect.ImmutableMap;
import net.minecraft.class_293;
import net.minecraft.class_296;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_293.class})
public interface VertexFormatAccessor {
    @Accessor
    public ImmutableMap<String, class_296> getElementMap();
}

