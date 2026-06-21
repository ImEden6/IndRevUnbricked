/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.api.registry;

public final class RegistryUtil {
    public static int createId(int objectPos, byte chunkPos) {
        if (chunkPos > 63) {
            throw new IllegalStateException("Chunk pos is too big. " + chunkPos + " > 63");
        }
        if (objectPos > 0x3FFFFFF) {
            throw new IllegalStateException("Object pos is too big. " + objectPos + " > 67108863");
        }
        return objectPos << 6 | chunkPos & 0x3F;
    }

    public static byte getChunkId(int id) {
        return (byte)(id & 0x3F);
    }

    public static int getObjectId(int id) {
        return id >>> 6;
    }
}

