/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.io.def;

import java.nio.ByteBuffer;

public class NativeImageData {
    public final ByteBuffer buffer;
    public final boolean stb;

    public NativeImageData(ByteBuffer buffer, boolean stb) {
        this.buffer = buffer;
        this.stb = stb;
    }
}

