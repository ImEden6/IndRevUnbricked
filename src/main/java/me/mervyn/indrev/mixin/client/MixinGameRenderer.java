package me.mervyn.indrev.mixin.client;

import me.mervyn.indrev.api.IRPlayerEntityExtension;
import me.mervyn.indrev.tools.modular.ArmorModule;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 999)
public class MixinGameRenderer {
    @Inject(method = "getNightVisionStrength", at = @At("INVOKE"), cancellable = true)
    private static void indrev_nightVisionStrength(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof IRPlayerEntityExtension && ((IRPlayerEntityExtension) livingEntity).isApplied(ArmorModule.NIGHT_VISION)) {
            cir.setReturnValue(1f);
        }
    }
}
