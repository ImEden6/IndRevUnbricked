/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1087
 *  net.minecraft.class_1095
 *  net.minecraft.class_2680
 *  org.apache.commons.lang3.tuple.Pair
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.notalpha.dashloader.mixin.accessor;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.class_1058;
import net.minecraft.class_1087;
import net.minecraft.class_1095;
import net.minecraft.class_2680;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1095.class})
public interface MultipartBakedModelAccessor {
    @Accessor
    public List<Pair<Predicate<class_2680>, class_1087>> getComponents();

    @Accessor
    @Mutable
    public void setComponents(List<Pair<Predicate<class_2680>, class_1087>> var1);

    @Accessor
    public Map<class_2680, BitSet> getStateCache();

    @Accessor
    @Mutable
    public void setStateCache(Map<class_2680, BitSet> var1);

    @Accessor
    @Mutable
    public void setSprite(class_1058 var1);
}

