/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.class_1060
 *  net.minecraft.class_2960
 *  net.minecraft.class_3300
 *  net.minecraft.class_3695
 *  net.minecraft.class_377
 *  net.minecraft.class_378
 *  net.minecraft.class_390
 *  net.minecraft.class_7191
 *  org.apache.commons.lang3.tuple.Pair
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.notalpha.dashloader.mixin.option.cache.font;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.client.font.FontModule;
import dev.notalpha.dashloader.mixin.accessor.FontManagerAccessor;
import dev.notalpha.dashloader.mixin.accessor.FontStorageAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1060;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import net.minecraft.class_377;
import net.minecraft.class_378;
import net.minecraft.class_390;
import net.minecraft.class_7191;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets={"net/minecraft/client/font/FontManager$1"})
public class FontManagerOverride {
    @Inject(method={"method_18638", "prepare*"}, at={@At(value="HEAD")}, cancellable=true)
    private void overridePrepare(class_3300 resourceManager, class_3695 profiler, CallbackInfoReturnable<Map<class_2960, List<class_390>>> cir) {
        FontModule.DATA.visit(CacheStatus.LOAD, data -> {
            DashLoader.LOG.info("Preparing fonts");
            Object2ObjectOpenHashMap out = new Object2ObjectOpenHashMap();
            data.forEach((arg_0, arg_1) -> FontManagerOverride.lambda$overridePrepare$0((Map)out, arg_0, arg_1));
            cir.setReturnValue((Object)out);
        });
    }

    @Inject(method={"method_18635", "apply*"}, at={@At(value="HEAD")}, cancellable=true)
    private void overrideApply(Map<class_2960, List<class_390>> map, class_3300 resourceManager, class_3695 profiler, CallbackInfo ci) {
        FontModule.DATA.visit(CacheStatus.LOAD, data -> {
            profiler.method_16065();
            profiler.method_15396("closing");
            FontManagerAccessor fontManagerAccessor = (FontManagerAccessor)FontModule.FONTMANAGER;
            Map<class_2960, class_377> fontStorages = fontManagerAccessor.getFontStorages();
            fontStorages.values().forEach(class_377::close);
            fontStorages.clear();
            DashLoader.LOG.info("Applying fonts off-thread");
            profiler.method_15405("reloading");
            data.forEach((identifier, entry) -> {
                class_377 fontStorage = new class_377(fontManagerAccessor.getTextureManager(), identifier);
                FontStorageAccessor access = (FontStorageAccessor)fontStorage;
                access.callCloseFonts();
                access.callCloseGlyphAtlases();
                access.getGlyphRendererCache().method_51597();
                access.getGlyphCache().method_51597();
                access.getCharactersByWidth().clear();
                access.setBlankGlyphRenderer(class_7191.field_37899.bake(access::callGetGlyphRenderer));
                access.setWhiteRectangleGlyphRenderer(class_7191.field_37898.bake(access::callGetGlyphRenderer));
                access.getCharactersByWidth().putAll((Map)entry.getKey());
                access.getFonts().addAll((Collection)entry.getValue());
                fontStorages.put((class_2960)identifier, fontStorage);
            });
            profiler.method_15407();
            profiler.method_16066();
            ci.cancel();
        });
    }

    @Inject(method={"method_18635", "apply*"}, at={@At(value="TAIL")})
    private void applyInject(Map<class_2960, List<class_390>> map, class_3300 resourceManager, class_3695 profiler, CallbackInfo ci) {
        FontModule.DATA.visit(CacheStatus.SAVE, data -> {
            data.clear();
            FontManagerAccessor fontManagerAccessor = (FontManagerAccessor)FontModule.FONTMANAGER;
            Map<class_2960, class_377> fontStorages = fontManagerAccessor.getFontStorages();
            fontStorages.forEach((identifier, fontStorage) -> {
                FontStorageAccessor access = (FontStorageAccessor)fontStorage;
                data.put(identifier, (Object)Pair.of(access.getCharactersByWidth(), access.getFonts()));
            });
        });
    }

    private static /* synthetic */ void lambda$overridePrepare$0(Map out, class_2960 identifier, Pair int2ObjectMapListPair) {
        out.put(identifier, (List)int2ObjectMapListPair.getValue());
    }

    @Mixin(value={class_378.class})
    private static class LeoFontSolution {
        private LeoFontSolution() {
        }

        @Inject(method={"<init>"}, at={@At(value="TAIL")})
        private void initInject(class_1060 manager, CallbackInfo ci) {
            FontModule.FONTMANAGER = (class_378)this;
        }
    }
}

