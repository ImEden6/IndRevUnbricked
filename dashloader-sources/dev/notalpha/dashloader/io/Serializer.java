/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.builtin.StepTask
 *  dev.quantumfusion.hyphen.HyphenSerializer
 *  dev.quantumfusion.hyphen.SerializerFactory
 *  dev.quantumfusion.hyphen.io.ByteBufferIO
 *  dev.quantumfusion.hyphen.io.IOInterface
 *  dev.quantumfusion.hyphen.scan.annotations.DataSubclasses
 *  net.minecraft.class_391$class_7736
 *  net.minecraft.class_391$class_8543
 *  net.minecraft.class_391$class_8544
 *  net.minecraft.class_391$class_8547
 *  org.jetbrains.annotations.NotNull
 */
package dev.notalpha.dashloader.io;

import dev.notalpha.dashloader.config.ConfigHandler;
import dev.notalpha.dashloader.io.IOHelper;
import dev.notalpha.dashloader.io.def.NativeImageData;
import dev.notalpha.dashloader.io.def.NativeImageDataDef;
import dev.notalpha.dashloader.registry.data.ChunkData;
import dev.notalpha.taski.builtin.StepTask;
import dev.quantumfusion.hyphen.HyphenSerializer;
import dev.quantumfusion.hyphen.SerializerFactory;
import dev.quantumfusion.hyphen.io.ByteBufferIO;
import dev.quantumfusion.hyphen.io.IOInterface;
import dev.quantumfusion.hyphen.scan.annotations.DataSubclasses;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.class_391;
import org.jetbrains.annotations.NotNull;

public class Serializer<O> {
    private final HyphenSerializer<ByteBufferIO, O> serializer;

    public Serializer(Class<O> aClass) {
        SerializerFactory factory = SerializerFactory.createDebug(ByteBufferIO.class, aClass);
        factory.addGlobalAnnotation(ChunkData.class, DataSubclasses.class, (Object)new Class[]{ChunkData.class});
        factory.setClassName(Serializer.getSerializerClassName(aClass));
        factory.addGlobalAnnotation(class_391.class_8544.class, DataSubclasses.class, (Object)new Class[]{class_391.class_8543.class, class_391.class_8547.class, class_391.class_7736.class});
        factory.addDynamicDef(NativeImageData.class, (clazz, serializerHandler) -> new NativeImageDataDef(serializerHandler, clazz));
        this.serializer = factory.build();
    }

    public O get(ByteBufferIO io) {
        return (O)this.serializer.get((IOInterface)io);
    }

    public void put(ByteBufferIO io, O data) {
        this.serializer.put((IOInterface)io, data);
    }

    public long measure(O data) {
        return this.serializer.measure(data);
    }

    public void save(Path path, StepTask task, O data) {
        int measure = (int)this.serializer.measure(data);
        ByteBufferIO io = ByteBufferIO.createDirect((int)measure);
        this.serializer.put((IOInterface)io, data);
        io.rewind();
        try {
            IOHelper.save(path, task, io, measure, ConfigHandler.INSTANCE.config.compression);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public O load(Path path) {
        try {
            ByteBufferIO io = IOHelper.load(path);
            return (O)this.serializer.get((IOInterface)io);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static <O> String getSerializerClassName(Class<O> holderClass) {
        return holderClass.getSimpleName().toLowerCase() + "-serializer";
    }
}

