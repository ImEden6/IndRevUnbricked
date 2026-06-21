/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7764$class_5791
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.mixin.accessor.SpriteAnimationFrameAccessor;
import net.minecraft.class_7764;

public final class DashSpriteAnimationFrame
implements DashObject<class_7764.class_5791> {
    public final int index;
    public final int time;

    public DashSpriteAnimationFrame(int index, int time) {
        this.index = index;
        this.time = time;
    }

    public DashSpriteAnimationFrame(class_7764.class_5791 animationFrame) {
        SpriteAnimationFrameAccessor access = (SpriteAnimationFrameAccessor)animationFrame;
        this.index = access.getIndex();
        this.time = access.getTime();
    }

    @Override
    public class_7764.class_5791 export(RegistryReader exportHandler) {
        return SpriteAnimationFrameAccessor.newSpriteFrame(this.index, this.time);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashSpriteAnimationFrame that = (DashSpriteAnimationFrame)o;
        if (this.index != that.index) {
            return false;
        }
        return this.time == that.time;
    }

    public int hashCode() {
        int result = this.index;
        result = 31 * result + this.time;
        return result;
    }
}

