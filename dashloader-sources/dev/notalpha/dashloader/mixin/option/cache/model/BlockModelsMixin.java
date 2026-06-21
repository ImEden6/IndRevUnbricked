/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1091
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_773
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.option.cache.model;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.model.ModelModule;
import net.minecraft.class_1091;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_773;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_773.class})
public class BlockModelsMixin {
    @Inject(method={"getModelId(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/util/ModelIdentifier;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void cacheModelId(class_2680 state, CallbackInfoReturnable<class_1091> cir) {
        ModelModule.MISSING_READ.visit(CacheStatus.LOAD, map -> {
            class_2960 identifier = (class_2960)map.get(state);
            if (identifier != null) {
                cir.setReturnValue((Object)((class_1091)identifier));
            }
        });
    }
}

