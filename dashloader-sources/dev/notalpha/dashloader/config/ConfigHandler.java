/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.fabricmc.loader.api.metadata.CustomValue
 *  net.fabricmc.loader.api.metadata.ModMetadata
 */
package dev.notalpha.dashloader.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.config.Config;
import dev.notalpha.dashloader.config.Option;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class ConfigHandler {
    private static final EnumMap<Option, Boolean> OPTION_ACTIVE = new EnumMap(Option.class);
    private static final String DISABLE_OPTION_TAG = "dashloader:disableoption";
    public static final ConfigHandler INSTANCE;
    private final Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private final Path configPath;
    public Config config = new Config();

    public ConfigHandler(Path configPath) {
        this.configPath = configPath;
        this.reloadConfig();
        this.config.options.forEach((s, aBoolean) -> {
            try {
                Option option = Option.valueOf(s.toUpperCase());
                OPTION_ACTIVE.put(option, false);
                DashLoader.LOG.warn("Disabled Optional Feature {} from DashLoader config.", s);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                DashLoader.LOG.error("Could not disable Optional Feature {} as it does not exist.", s);
            }
        });
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            ModMetadata mod = modContainer.getMetadata();
            if (!mod.containsCustomValue(DISABLE_OPTION_TAG)) continue;
            for (CustomValue value : mod.getCustomValue(DISABLE_OPTION_TAG).getAsArray()) {
                String feature = value.getAsString();
                try {
                    Option option = Option.valueOf(feature.toUpperCase());
                    OPTION_ACTIVE.put(option, false);
                    DashLoader.LOG.warn("Disabled Optional Feature {} from {} config. {}", (Object)feature, (Object)mod.getId(), (Object)mod.getName());
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    DashLoader.LOG.error("Could not disable Optional Feature {} as it does not exist.", (Object)feature);
                }
            }
        }
    }

    public void reloadConfig() {
        try {
            if (Files.exists(this.configPath, new LinkOption[0])) {
                BufferedReader json = Files.newBufferedReader(this.configPath);
                this.config = (Config)this.gson.fromJson((Reader)json, Config.class);
                json.close();
            }
        }
        catch (Throwable err) {
            DashLoader.LOG.info("Config corrupted creating a new one.", err);
        }
        this.saveConfig();
    }

    public void saveConfig() {
        try {
            Files.createDirectories(this.configPath.getParent(), new FileAttribute[0]);
            Files.deleteIfExists(this.configPath);
            BufferedWriter writer = Files.newBufferedWriter(this.configPath, StandardOpenOption.CREATE);
            this.gson.toJson((Object)this.config, (Appendable)writer);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldApplyMixin(String name) {
        for (Option value : Option.values()) {
            if (!name.contains(value.mixinContains)) continue;
            return OPTION_ACTIVE.get((Object)value);
        }
        return true;
    }

    public static boolean optionActive(Option option) {
        return OPTION_ACTIVE.get((Object)option);
    }

    static {
        for (Option value : Option.values()) {
            OPTION_ACTIVE.put(value, true);
        }
        INSTANCE = new ConfigHandler(FabricLoader.getInstance().getConfigDir().normalize().resolve("dashloader.json"));
    }
}

