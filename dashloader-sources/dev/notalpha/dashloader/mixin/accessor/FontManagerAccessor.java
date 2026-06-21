/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1060
 *  net.minecraft.class_2960
 *  net.minecraft.class_377
 *  net.minecraft.class_378
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.Map;
import net.minecraft.class_1060;
import net.minecraft.class_2960;
import net.minecraft.class_377;
import net.minecraft.class_378;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_378.class})
public interface FontManagerAccessor {
    @Accessor
    public class_1060 getTextureManager();

    @Accessor
    public Map<class_2960, class_377> getFontStorages();
}

