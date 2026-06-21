/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3258
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.io.File;
import net.minecraft.class_3258;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_3258.class})
public interface ZipResourcePackAccessor {
    @Accessor
    public File getBackingZipFile();
}

