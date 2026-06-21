/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.fabricmc.loader.api.metadata.ModMetadata
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package dev.notalpha.dashloader;

import dev.notalpha.dashloader.io.Serializer;
import dev.notalpha.dashloader.io.data.CacheInfo;
import java.util.ArrayList;
import java.util.Comparator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DashLoader {
    private static final String VERSION;
    public static final Logger LOG;
    public static final Serializer<CacheInfo> METADATA_SERIALIZER;
    public static final String MOD_HASH;

    public static void bootstrap() {
    }

    private DashLoader() {
        LOG.info("Initializing DashLoader " + VERSION + ".");
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOG.warn("DashLoader launched in dev.");
        }
    }

    static {
        ModMetadata metadata;
        VERSION = ((ModContainer)FabricLoader.getInstance().getModContainer("dashloader").orElseThrow(() -> new IllegalStateException("DashLoader not found... apparently! WTF?"))).getMetadata().getVersion().getFriendlyString();
        LOG = LogManager.getLogger((String)"DashLoader");
        METADATA_SERIALIZER = new Serializer<CacheInfo>(CacheInfo.class);
        ArrayList<ModMetadata> versions = new ArrayList<ModMetadata>();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            metadata = mod.getMetadata();
            versions.add(metadata);
        }
        versions.sort(Comparator.comparing(ModMetadata::getId));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < versions.size(); ++i) {
            metadata = (ModMetadata)versions.get(i);
            stringBuilder.append(i).append("$").append(metadata.getId()).append('&').append(metadata.getVersion().getFriendlyString());
        }
        MOD_HASH = DigestUtils.md5Hex((String)stringBuilder.toString()).toUpperCase();
    }
}

