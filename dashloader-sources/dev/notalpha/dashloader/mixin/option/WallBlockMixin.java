/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.minecraft.class_2248
 *  net.minecraft.class_2544
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2746
 *  net.minecraft.class_2754
 *  net.minecraft.class_2769
 *  net.minecraft.class_3737
 *  net.minecraft.class_4778
 *  net.minecraft.class_4970$class_2251
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.option;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.class_2248;
import net.minecraft.class_2544;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2746;
import net.minecraft.class_2754;
import net.minecraft.class_2769;
import net.minecraft.class_3737;
import net.minecraft.class_4778;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_2544.class})
public abstract class WallBlockMixin
extends class_2248
implements class_3737 {
    private static final int LENGTH = class_4778.values().length;
    private static class_265[][][][][] SHAPE_CACHE;
    private static class_265[][][][][] COLLISION_CACHE;
    @Shadow
    @Final
    public static class_2746 field_11717;
    @Shadow
    @Final
    public static class_2754<class_4778> field_22156;
    @Shadow
    @Final
    public static class_2754<class_4778> field_22157;
    @Shadow
    @Final
    public static class_2754<class_4778> field_22159;
    @Shadow
    @Final
    public static class_2754<class_4778> field_22158;
    @Shadow
    @Final
    public static class_2746 field_22160;

    public WallBlockMixin(class_4970.class_2251 settings) {
        super(settings);
    }

    @Inject(method={"getShapeMap"}, at={@At(value="HEAD")}, cancellable=true)
    private void getShapeMapCache(float f, float g, float h, float i, float j, float k, CallbackInfoReturnable<Map<class_2680, class_265>> cir) {
        if (this.isCommon(f, g, i)) {
            if (this.isShape(h, j, k)) {
                if (SHAPE_CACHE != null) {
                    cir.setReturnValue(this.createFromCache(SHAPE_CACHE));
                }
            } else if (this.isCollision(h, j, k) && COLLISION_CACHE != null) {
                cir.setReturnValue(this.createFromCache(COLLISION_CACHE));
            }
        }
    }

    @Inject(method={"getShapeMap"}, at={@At(value="RETURN")})
    private void getShapeMapCacheCreate(float f, float g, float h, float i, float j, float k, CallbackInfoReturnable<Map<class_2680, class_265>> cir) {
        if ((SHAPE_CACHE == null || COLLISION_CACHE == null) && this.isCommon(f, g, i)) {
            if (this.isShape(h, j, k)) {
                if (SHAPE_CACHE == null) {
                    SHAPE_CACHE = new class_265[2][LENGTH][LENGTH][LENGTH][LENGTH];
                    this.createCache(SHAPE_CACHE, (Map)cir.getReturnValue());
                }
            } else if (this.isCollision(h, j, k) && COLLISION_CACHE == null) {
                COLLISION_CACHE = new class_265[2][LENGTH][LENGTH][LENGTH][LENGTH];
                this.createCache(COLLISION_CACHE, (Map)cir.getReturnValue());
            }
        }
    }

    private ImmutableMap<class_2680, class_265> createFromCache(class_265[][][][][] rawCache) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (Boolean up : field_11717.method_11898()) {
            class_265[][][][] cache = up != false ? rawCache[1] : rawCache[0];
            for (class_4778 east : field_22156.method_11898()) {
                for (class_4778 north : field_22157.method_11898()) {
                    for (class_4778 west : field_22159.method_11898()) {
                        for (class_4778 south : field_22158.method_11898()) {
                            class_265 cached = this.getCached(cache, east, north, west, south);
                            class_2680 blockState = (class_2680)((class_2680)((class_2680)((class_2680)((class_2680)this.method_9564().method_11657((class_2769)field_11717, (Comparable)up)).method_11657(field_22156, (Comparable)east)).method_11657(field_22159, (Comparable)west)).method_11657(field_22157, (Comparable)north)).method_11657(field_22158, (Comparable)south);
                            builder.put((Object)((class_2680)blockState.method_11657((class_2769)field_22160, (Comparable)Boolean.valueOf(false))), (Object)cached);
                            builder.put((Object)((class_2680)blockState.method_11657((class_2769)field_22160, (Comparable)Boolean.valueOf(true))), (Object)cached);
                        }
                    }
                }
            }
        }
        return builder.build();
    }

    private void createCache(class_265[][][][][] rawCache, Map<class_2680, class_265> map) {
        for (Boolean up : field_11717.method_11898()) {
            class_265[][][][] cache = up != false ? rawCache[1] : rawCache[0];
            for (class_4778 east : field_22156.method_11898()) {
                for (class_4778 north : field_22157.method_11898()) {
                    for (class_4778 west : field_22159.method_11898()) {
                        for (class_4778 south : field_22158.method_11898()) {
                            class_2680 blockState = (class_2680)((class_2680)((class_2680)((class_2680)((class_2680)((class_2680)this.method_9564().method_11657((class_2769)field_11717, (Comparable)up)).method_11657(field_22156, (Comparable)east)).method_11657(field_22159, (Comparable)west)).method_11657(field_22157, (Comparable)north)).method_11657(field_22158, (Comparable)south)).method_11657((class_2769)field_22160, (Comparable)Boolean.valueOf(false));
                            this.setCached(cache, east, north, west, south, map.get(blockState));
                        }
                    }
                }
            }
        }
    }

    private boolean isShape(float h, float j, float k) {
        return h == 16.0f && j == 14.0f && k == 16.0f;
    }

    private boolean isCollision(float h, float j, float k) {
        return h == 24.0f && j == 24.0f && k == 24.0f;
    }

    private boolean isCommon(float f, float g, float i) {
        return f == 4.0f && g == 3.0f && i == 0.0f;
    }

    private class_265 getCached(class_265[][][][] cache, class_4778 east, class_4778 north, class_4778 west, class_4778 south) {
        return cache[east.ordinal()][north.ordinal()][west.ordinal()][south.ordinal()];
    }

    private void setCached(class_265[][][][] cache, class_4778 east, class_4778 north, class_4778 west, class_4778 south, class_265 shape) {
        cache[east.ordinal()][north.ordinal()][west.ordinal()][south.ordinal()] = shape;
    }
}

