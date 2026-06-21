/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_1091
 *  net.minecraft.class_2960
 *  net.minecraft.class_812
 *  net.minecraft.class_815
 *  net.minecraft.class_818
 *  net.minecraft.class_821
 */
package dev.notalpha.dashloader.client;

import dev.notalpha.dashloader.api.DashEntrypoint;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheFactory;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.blockstate.DashBlockState;
import dev.notalpha.dashloader.client.font.DashBitmapFont;
import dev.notalpha.dashloader.client.font.DashBlankFont;
import dev.notalpha.dashloader.client.font.DashSpaceFont;
import dev.notalpha.dashloader.client.font.DashTrueTypeFont;
import dev.notalpha.dashloader.client.font.DashUnihexFont;
import dev.notalpha.dashloader.client.font.FontModule;
import dev.notalpha.dashloader.client.identifier.DashIdentifier;
import dev.notalpha.dashloader.client.identifier.DashModelIdentifier;
import dev.notalpha.dashloader.client.model.DashBasicBakedModel;
import dev.notalpha.dashloader.client.model.DashBuiltinBakedModel;
import dev.notalpha.dashloader.client.model.DashMultipartBakedModel;
import dev.notalpha.dashloader.client.model.DashWeightedBakedModel;
import dev.notalpha.dashloader.client.model.ModelModule;
import dev.notalpha.dashloader.client.model.components.DashBakedQuad;
import dev.notalpha.dashloader.client.model.components.DashBakedQuadCollection;
import dev.notalpha.dashloader.client.model.predicates.BooleanSelector;
import dev.notalpha.dashloader.client.model.predicates.DashAndPredicate;
import dev.notalpha.dashloader.client.model.predicates.DashOrPredicate;
import dev.notalpha.dashloader.client.model.predicates.DashSimplePredicate;
import dev.notalpha.dashloader.client.model.predicates.DashStaticPredicate;
import dev.notalpha.dashloader.client.shader.DashShader;
import dev.notalpha.dashloader.client.shader.DashVertexFormat;
import dev.notalpha.dashloader.client.shader.ShaderModule;
import dev.notalpha.dashloader.client.splash.SplashModule;
import dev.notalpha.dashloader.client.sprite.DashImage;
import dev.notalpha.dashloader.client.sprite.DashSprite;
import dev.notalpha.dashloader.client.sprite.SpriteModule;
import java.nio.file.Path;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1091;
import net.minecraft.class_2960;
import net.minecraft.class_812;
import net.minecraft.class_815;
import net.minecraft.class_818;
import net.minecraft.class_821;

public class DashLoaderClient
implements DashEntrypoint {
    public static final Cache CACHE;
    public static boolean NEEDS_RELOAD;

    @Override
    public void onDashLoaderInit(CacheFactory factory) {
        factory.addModule(new FontModule());
        factory.addModule(new ModelModule());
        factory.addModule(new ShaderModule());
        factory.addModule(new SplashModule());
        factory.addModule(new SpriteModule());
        factory.addMissingHandler(class_2960.class, (identifier, registryWriter) -> {
            if (identifier instanceof class_1091) {
                class_1091 m = (class_1091)identifier;
                return new DashModelIdentifier(m);
            }
            return new DashIdentifier((class_2960)identifier);
        });
        factory.addMissingHandler(class_815.class, (selector, writer) -> {
            if (selector == class_815.field_16900) {
                return new DashStaticPredicate(true);
            }
            if (selector == class_815.field_16901) {
                return new DashStaticPredicate(false);
            }
            if (selector instanceof class_812) {
                class_812 s = (class_812)selector;
                return new DashAndPredicate(s, (RegistryWriter)writer);
            }
            if (selector instanceof class_821) {
                class_821 s = (class_821)selector;
                return new DashOrPredicate(s, (RegistryWriter)writer);
            }
            if (selector instanceof class_818) {
                class_818 s = (class_818)selector;
                return new DashSimplePredicate(s);
            }
            if (selector instanceof BooleanSelector) {
                BooleanSelector s = (BooleanSelector)selector;
                return new DashStaticPredicate(s.selector);
            }
            throw new RuntimeException("someone is having fun with lambda selectors again");
        });
        for (Class aClass : new Class[]{DashIdentifier.class, DashModelIdentifier.class, DashBasicBakedModel.class, DashBuiltinBakedModel.class, DashMultipartBakedModel.class, DashWeightedBakedModel.class, DashBakedQuad.class, DashBakedQuadCollection.class, DashAndPredicate.class, DashOrPredicate.class, DashSimplePredicate.class, DashStaticPredicate.class, DashImage.class, DashSprite.class, DashBitmapFont.class, DashBlankFont.class, DashSpaceFont.class, DashTrueTypeFont.class, DashUnihexFont.class, DashBlockState.class, DashVertexFormat.class, DashShader.class}) {
            factory.addDashObject(aClass);
        }
    }

    static {
        NEEDS_RELOAD = false;
        CacheFactory cacheManagerFactory = CacheFactory.create();
        List entryPoints = FabricLoader.getInstance().getEntrypoints("dashloader", DashEntrypoint.class);
        for (DashEntrypoint entryPoint : entryPoints) {
            entryPoint.onDashLoaderInit(cacheManagerFactory);
        }
        CACHE = cacheManagerFactory.build(Path.of("./dashloader-cache/client/", new String[0]));
    }
}

