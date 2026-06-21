/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.thr.HyphenException
 *  net.minecraft.class_1088
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2960
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import dev.quantumfusion.hyphen.thr.HyphenException;
import java.util.Map;
import net.minecraft.class_1088;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1088.class})
public interface ModelLoaderAccessor {
    @Accessor(value="ITEM_FRAME_STATE_FACTORY")
    public static class_2689<class_2248, class_2680> getTheItemFrameThing() {
        throw new HyphenException("froge", "your dad");
    }

    @Accessor(value="STATIC_DEFINITIONS")
    public static Map<class_2960, class_2689<class_2248, class_2680>> getStaticDefinitions() {
        throw new HyphenException("froge", "your dad");
    }
}

