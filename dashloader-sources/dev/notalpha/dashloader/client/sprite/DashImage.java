/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_1011$class_1012
 *  org.lwjgl.system.MemoryUtil
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.io.def.NativeImageData;
import dev.notalpha.dashloader.mixin.accessor.NativeImageAccessor;
import java.nio.ByteBuffer;
import net.minecraft.class_1011;
import org.lwjgl.system.MemoryUtil;

public final class DashImage
implements DashObject<class_1011> {
    public final NativeImageData image;
    public final class_1011.class_1012 format;
    public final boolean useSTB;
    public final int width;
    public final int height;

    public DashImage(class_1011 nativeImage) {
        NativeImageAccessor nativeImageAccess = (NativeImageAccessor)nativeImage;
        this.format = nativeImage.method_4318();
        this.width = nativeImage.method_4307();
        this.height = nativeImage.method_4323();
        int capacity = this.width * this.height * this.format.method_4335();
        long pointer = nativeImageAccess.getPointer();
        this.useSTB = nativeImageAccess.getIsStbImage();
        ByteBuffer image1 = MemoryUtil.memByteBuffer((long)pointer, (int)capacity);
        image1.limit(capacity);
        this.image = new NativeImageData(image1, this.useSTB);
    }

    public DashImage(NativeImageData image, class_1011.class_1012 format, boolean useSTB, int width, int height) {
        this.image = image;
        this.format = format;
        this.useSTB = useSTB;
        this.width = width;
        this.height = height;
    }

    @Override
    public class_1011 export(RegistryReader registry) {
        this.image.buffer.rewind();
        long pointer = MemoryUtil.memAddress((ByteBuffer)this.image.buffer);
        return NativeImageAccessor.init(this.format, this.width, this.height, this.useSTB, pointer);
    }
}

