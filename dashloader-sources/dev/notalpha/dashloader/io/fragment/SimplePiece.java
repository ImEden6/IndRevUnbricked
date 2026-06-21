/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.io.fragment;

import dev.notalpha.dashloader.io.fragment.Piece;
import java.util.Arrays;

public class SimplePiece
extends Piece {
    public final Piece[] value;

    public SimplePiece(Piece[] value) {
        super(Arrays.stream(value).mapToLong(dEntry -> dEntry.size).sum());
        this.value = value;
    }

    @Override
    public Piece[] getInner() {
        return this.value;
    }

    public String toString() {
        return Arrays.toString(this.value);
    }
}

