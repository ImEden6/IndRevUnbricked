/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1087
 *  net.minecraft.class_1095
 *  net.minecraft.class_1095$class_1096
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2960
 *  net.minecraft.class_3665
 *  net.minecraft.class_4730
 *  net.minecraft.class_7775
 *  net.minecraft.class_816
 *  net.minecraft.class_819
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 *  org.spongepowered.asm.mixin.injection.callback.LocalCapture
 */
package dev.notalpha.dashloader.mixin.option.cache.model;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.model.ModelModule;
import dev.notalpha.dashloader.mixin.accessor.MultipartModelComponentAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.class_1058;
import net.minecraft.class_1087;
import net.minecraft.class_1095;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2960;
import net.minecraft.class_3665;
import net.minecraft.class_4730;
import net.minecraft.class_7775;
import net.minecraft.class_816;
import net.minecraft.class_819;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={class_816.class})
public class MultipartUnbakedModelMixin {
    @Shadow
    @Final
    private List<class_819> field_4330;
    @Shadow
    @Final
    private class_2689<class_2248, class_2680> field_4329;

    @Inject(method={"bake"}, at={@At(value="RETURN")}, locals=LocalCapture.CAPTURE_FAILSOFT, cancellable=true)
    private void addPredicateInfo(class_7775 baker, Function<class_4730, class_1058> textureGetter, class_3665 rotationContainer, class_2960 modelId, CallbackInfoReturnable<@Nullable class_1087> cir, class_1095.class_1096 builder) {
        ModelModule.MULTIPART_PREDICATES.visit(CacheStatus.SAVE, map -> {
            class_1095 bakedModel = (class_1095)builder.method_4750();
            ArrayList outSelectors = new ArrayList();
            this.field_4330.forEach(multipartModelComponent -> outSelectors.add(((MultipartModelComponentAccessor)multipartModelComponent).getSelector()));
            map.put(bakedModel, Pair.of(outSelectors, this.field_4329));
            cir.setReturnValue((Object)bakedModel);
        });
    }
}

