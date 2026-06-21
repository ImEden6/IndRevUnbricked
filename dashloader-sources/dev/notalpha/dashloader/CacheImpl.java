/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.StepTask
 *  org.apache.commons.io.FileUtils
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.DashObjectClass;
import dev.notalpha.dashloader.api.DashModule;
import dev.notalpha.dashloader.api.cache.Cache;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.io.MappingSerializer;
import dev.notalpha.dashloader.io.RegistrySerializer;
import dev.notalpha.dashloader.io.data.CacheInfo;
import dev.notalpha.dashloader.misc.ProfilerUtil;
import dev.notalpha.dashloader.registry.MissingHandler;
import dev.notalpha.dashloader.registry.RegistryReaderImpl;
import dev.notalpha.dashloader.registry.RegistryWriterImpl;
import dev.notalpha.dashloader.registry.data.StageData;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.StepTask;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

public final class CacheImpl
implements Cache {
    private static final String METADATA_FILE_NAME = "metadata.bin";
    private CacheStatus status;
    private String hash;
    private final Path cacheDir;
    private final List<DashModule<?>> cacheHandlers;
    private final List<DashObjectClass<?, ?>> dashObjects;
    private final List<MissingHandler<?>> missingHandlers;
    private final RegistrySerializer registrySerializer;
    private final MappingSerializer mappingsSerializer;

    CacheImpl(Path cacheDir, List<DashModule<?>> cacheHandlers, List<DashObjectClass<?, ?>> dashObjects, List<MissingHandler<?>> missingHandlers) {
        this.cacheDir = cacheDir;
        this.cacheHandlers = cacheHandlers;
        this.dashObjects = dashObjects;
        this.missingHandlers = missingHandlers;
        this.registrySerializer = new RegistrySerializer(dashObjects);
        this.mappingsSerializer = new MappingSerializer(cacheHandlers);
    }

    @Override
    public void load(String name) {
        this.hash = name;
        if (this.exists()) {
            this.setStatus(CacheStatus.LOAD);
            this.loadCache();
        } else {
            this.setStatus(CacheStatus.SAVE);
        }
    }

    @Override
    public boolean save(@Nullable Consumer<StepTask> taskConsumer) {
        if (this.status != CacheStatus.SAVE) {
            throw new RuntimeException("Status is not SAVE");
        }
        DashLoader.LOG.info("Starting DashLoader Caching");
        try {
            Path ourDir = this.getDir();
            int maxCaches = ConfigHandler.INSTANCE.config.maxCaches;
            if (maxCaches != -1) {
                DashLoader.LOG.info("Checking for cache count.");
                try {
                    FileTime oldestTime = null;
                    Path oldestPath = null;
                    int cacheCount = 1;
                    try (Stream<Path> stream = Files.list(this.cacheDir);){
                        for (Path path : stream.toList()) {
                            if (!Files.isDirectory(path, new LinkOption[0]) || path.equals(ourDir)) continue;
                            ++cacheCount;
                            try {
                                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
                                FileTime lastAccessTime = attrs.lastAccessTime();
                                if (oldestTime != null && lastAccessTime.compareTo(oldestTime) >= 0) continue;
                                oldestTime = lastAccessTime;
                                oldestPath = path;
                            }
                            catch (IOException e) {
                                DashLoader.LOG.warn("Could not find access time for cache.", (Throwable)e);
                            }
                        }
                    }
                    if (oldestPath != null && cacheCount > maxCaches) {
                        DashLoader.LOG.info("Removing {} as we are currently above the maximum caches.", oldestPath);
                        if (!FileUtils.deleteQuietly((File)oldestPath.toFile())) {
                            DashLoader.LOG.error("Could not remove cache {}", (Object)oldestPath);
                        }
                    }
                }
                catch (NoSuchFileException oldestTime) {
                }
                catch (IOException io) {
                    DashLoader.LOG.error("Could not enforce maximum cache ", (Throwable)io);
                }
            }
            long start = System.currentTimeMillis();
            StepTask main = new StepTask("save", 2);
            if (taskConsumer != null) {
                taskConsumer.accept(main);
            }
            RegistryWriterImpl factory = RegistryWriterImpl.create(this.missingHandlers, this.dashObjects);
            this.mappingsSerializer.save(ourDir, factory, this.cacheHandlers, main);
            main.next();
            main.run((Task)new StepTask("serialize", 2), task -> {
                try {
                    CacheInfo info = this.registrySerializer.serialize(ourDir, factory, arg_0 -> ((StepTask)task).setSubTask(arg_0));
                    task.next();
                    DashLoader.METADATA_SERIALIZER.save(ourDir.resolve(METADATA_FILE_NAME), new StepTask("hi"), info);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                task.next();
            });
            DashLoader.LOG.info("Saved cache in " + ProfilerUtil.getTimeStringFromStart(start));
            return true;
        }
        catch (Throwable thr) {
            DashLoader.LOG.error("Failed caching", thr);
            this.setStatus(CacheStatus.SAVE);
            this.remove();
            return false;
        }
    }

    private void loadCache() {
        if (this.status != CacheStatus.LOAD) {
            throw new RuntimeException("Status is not LOAD");
        }
        long start = System.currentTimeMillis();
        try {
            StepTask task = new StepTask("Loading DashCache", 3);
            Path cacheDir = this.getDir();
            Path metadataPath = cacheDir.resolve(METADATA_FILE_NAME);
            CacheInfo info = DashLoader.METADATA_SERIALIZER.load(metadataPath);
            StageData[] stageData = this.registrySerializer.deserialize(cacheDir, info, this.dashObjects);
            RegistryReaderImpl reader = new RegistryReaderImpl(info, stageData);
            task.run(() -> reader.export(arg_0 -> ((StepTask)task).setSubTask(arg_0)));
            if (!this.mappingsSerializer.load(cacheDir, reader, this.cacheHandlers)) {
                this.setStatus(CacheStatus.SAVE);
                this.remove();
                return;
            }
            DashLoader.LOG.info("Loaded cache in {}", (Object)ProfilerUtil.getTimeStringFromStart(start));
        }
        catch (Exception e) {
            DashLoader.LOG.error("Summoned CrashLoader in {}", (Object)ProfilerUtil.getTimeStringFromStart(start), (Object)e);
            this.setStatus(CacheStatus.SAVE);
            this.remove();
        }
    }

    public boolean exists() {
        return Files.exists(this.getDir(), new LinkOption[0]);
    }

    @Override
    public void remove() {
        try {
            FileUtils.deleteDirectory((File)this.getDir().toFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        this.setStatus(CacheStatus.IDLE);
    }

    @Override
    public Path getDir() {
        if (this.hash == null) {
            throw new RuntimeException("Cache hash has not been set.");
        }
        return this.cacheDir.resolve(this.hash + "/");
    }

    @Override
    public CacheStatus getStatus() {
        return this.status;
    }

    private void setStatus(CacheStatus status) {
        if (this.status != status) {
            this.status = status;
            DashLoader.LOG.info("\u001b[46m\u001b[30m DashLoader Status change {}\n\u001b[0m", (Object)status);
            this.cacheHandlers.forEach(handler -> handler.reset(this));
        }
    }
}

