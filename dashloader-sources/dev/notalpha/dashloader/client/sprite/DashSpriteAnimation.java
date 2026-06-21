/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7764
 *  net.minecraft.class_7764$class_5790
 *  net.minecraft.class_7764$class_5791
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.client.sprite.DashSpriteAnimationFrame;
import dev.notalpha.dashloader.mixin.accessor.SpriteAnimationAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_7764;

public final class DashSpriteAnimation {
    public final List<DashSpriteAnimationFrame> frames;
    public final int frameCount;
    public final boolean interpolation;

    public DashSpriteAnimation(List<DashSpriteAnimationFrame> frames, int frameCount, boolean interpolation) {
        this.frames = frames;
        this.frameCount = frameCount;
        this.interpolation = interpolation;
    }

    public DashSpriteAnimation(class_7764.class_5790 animation) {
        SpriteAnimationAccessor access = (SpriteAnimationAccessor)animation;
        this.frames = new ArrayList<DashSpriteAnimationFrame>();
        for (class_7764.class_5791 frame : access.getFrames()) {
            this.frames.add(new DashSpriteAnimationFrame(frame));
        }
        this.frameCount = access.getFrameCount();
        this.interpolation = access.getInterpolation();
    }

    public class_7764.class_5790 export(class_7764 owner, RegistryReader registry) {
        ArrayList<class_7764.class_5791> framesOut = new ArrayList<class_7764.class_5791>();
        for (DashSpriteAnimationFrame frame : this.frames) {
            framesOut.add(frame.export(registry));
        }
        return SpriteAnimationAccessor.init(owner, framesOut, this.frameCount, this.interpolation);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashSpriteAnimation that = (DashSpriteAnimation)o;
        if (this.frameCount != that.frameCount) {
            return false;
        }
        if (this.interpolation != that.interpolation) {
            return false;
        }
        return this.frames.equals(that.frames);
    }

    public int hashCode() {
        int result = this.frames.hashCode();
        result = 31 * result + this.frameCount;
        result = 31 * result + (this.interpolation ? 1 : 0);
        return result;
    }
}

