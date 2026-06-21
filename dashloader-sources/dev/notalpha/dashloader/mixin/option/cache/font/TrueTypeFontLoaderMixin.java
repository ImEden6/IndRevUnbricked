/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_3300
 *  net.minecraft.class_390
 *  net.minecraft.class_8557
 *  org.lwjgl.stb.STBTTFontinfo
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 *  org.spongepowered.asm.mixin.injection.callback.LocalCapture
 */
package dev.notalpha.dashloader.mixin.option.cache.font;

import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.font.FontModule;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_390;
import net.minecraft.class_8557;
import org.lwjgl.stb.STBTTFontinfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={class_8557.class})
public abstract class TrueTypeFontLoaderMixin {
    @Shadow
    public abstract class_2960 comp_1524();

    @Inject(method={"load"}, at={@At(value="INVOKE", target="Lcom/mojang/blaze3d/platform/TextureUtil;readResource(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;")}, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void loadInject(class_3300 manager, CallbackInfoReturnable<class_390> cir, STBTTFontinfo sTBTTFontinfo) {
        FontModule.FONT_TO_IDENT.visit(CacheStatus.SAVE, map -> map.put(sTBTTFontinfo, this.comp_1524()));
    }
}

