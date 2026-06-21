/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.io.fragment;

import dev.notalpha.dashloader.io.fragment.Fragment;
import java.util.ArrayList;

public abstract class Piece {
    public final long size;
    int elementPos = 0;

    protected Piece(long size) {
        this.size = size;
    }

    public abstract Piece[] getInner();

    public boolean isDone() {
        Piece[] inner = this.getInner();
        return inner == null || this.elementPos >= inner.length;
    }

    public Fragment fragment(long sizeRemaining) {
        Piece[] inner = this.getInner();
        if (inner == null) {
            throw new RuntimeException("Non splitting piece requested fragmentation");
        }
        int rangeStart = this.elementPos;
        long currentSize = 0L;
        ArrayList<Fragment> innerOut = new ArrayList<Fragment>();
        int rangeEnd = 0;
        while (currentSize < sizeRemaining && this.elementPos < inner.length) {
            Piece piece = inner[this.elementPos];
            rangeEnd = this.elementPos + 1;
            if (piece.getInner() == null) {
                currentSize += piece.size;
                ++this.elementPos;
                continue;
            }
            Fragment fragment = piece.fragment(sizeRemaining);
            innerOut.add(fragment);
            currentSize += fragment.size;
            if (!piece.isDone()) continue;
            ++this.elementPos;
        }
        return new Fragment(currentSize, rangeStart, rangeEnd, innerOut);
    }
}

