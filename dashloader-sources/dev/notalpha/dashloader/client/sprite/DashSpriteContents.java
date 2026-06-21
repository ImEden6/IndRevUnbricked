/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_1011
 *  net.minecraft.class_2960
 *  net.minecraft.class_7764
 *  net.minecraft.class_7764$class_5790
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.sprite.DashSpriteAnimation;
import dev.notalpha.dashloader.misc.UnsafeHelper;
import dev.notalpha.dashloader.mixin.accessor.SpriteContentsAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.Objects;
import net.minecraft.class_1011;
import net.minecraft.class_2960;
import net.minecraft.class_7764;
import org.jetbrains.annotations.Nullable;

public final class DashSpriteContents {
    public final int id;
    public final int image;
    public final @DataNullable @Nullable DashSpriteAnimation animation;
    public final int width;
    public final int height;

    public DashSpriteContents(int id, int image, @Nullable DashSpriteAnimation animation, int width, int height) {
        this.id = id;
        this.image = image;
        this.animation = animation;
        this.width = width;
        this.height = height;
    }

    public DashSpriteContents(class_7764 contents, RegistryWriter writer) {
        SpriteContentsAccessor access = (SpriteContentsAccessor)contents;
        this.id = writer.add(contents.method_45816());
        this.image = writer.add(access.getImage());
        this.width = contents.method_45807();
        this.height = contents.method_45815();
        class_7764.class_5790 animation = access.getAnimation();
        this.animation = animation == null ? null : new DashSpriteAnimation(animation);
    }

    public class_7764 export(RegistryReader reader) {
        class_7764 out = UnsafeHelper.allocateInstance(class_7764.class);
        SpriteContentsAccessor access = (SpriteContentsAccessor)out;
        access.setId((class_2960)reader.get(this.id));
        class_1011 image = (class_1011)reader.get(this.image);
        access.setImage(image);
        access.setHeight(this.height);
        access.setWidth(this.width);
        access.setMipmapLevelsImages(new class_1011[]{image});
        access.setAnimation(this.animation == null ? null : this.animation.export(out, reader));
        return out;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashSpriteContents that = (DashSpriteContents)o;
        if (this.id != that.id) {
            return false;
        }
        if (this.image != that.image) {
            return false;
        }
        if (this.width != that.width) {
            return false;
        }
        if (this.height != that.height) {
            return false;
        }
        return Objects.equals(this.animation, that.animation);
    }

    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.image;
        result = 31 * result + (this.animation != null ? this.animation.hashCode() : 0);
        result = 31 * result + this.width;
        result = 31 * result + this.height;
        return result;
    }
}

