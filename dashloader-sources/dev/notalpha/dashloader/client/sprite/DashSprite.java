/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2960
 *  net.minecraft.class_7766$class_7767
 */
package dev.notalpha.dashloader.client.sprite;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.sprite.DashSpriteContents;
import dev.notalpha.dashloader.client.sprite.SpriteModule;
import dev.notalpha.dashloader.mixin.accessor.SpriteAccessor;
import net.minecraft.class_1058;
import net.minecraft.class_2960;
import net.minecraft.class_7766;

public class DashSprite
implements DashObject<class_1058> {
    public final int atlasId;
    public final DashSpriteContents contents;
    public final int x;
    public final int y;
    public final int atlasWidth;
    public final int atlasHeight;

    public DashSprite(int atlasId, DashSpriteContents contents, int x, int y, int atlasWidth, int atlasHeight) {
        this.atlasId = atlasId;
        this.contents = contents;
        this.x = x;
        this.y = y;
        this.atlasWidth = atlasWidth;
        this.atlasHeight = atlasHeight;
    }

    public DashSprite(class_1058 sprite, RegistryWriter writer) {
        this.atlasId = writer.add(sprite.method_45852());
        this.contents = new DashSpriteContents(sprite.method_45851(), writer);
        class_2960 identifier = SpriteModule.ATLAS_IDS.get(CacheStatus.SAVE).get(sprite.method_45852());
        class_7766.class_7767 atlas = SpriteModule.ATLASES.get(CacheStatus.SAVE).get(identifier);
        this.x = sprite.method_35806();
        this.y = sprite.method_35807();
        this.atlasWidth = atlas.comp_1040();
        this.atlasHeight = atlas.comp_1041();
    }

    @Override
    public class_1058 export(RegistryReader registry) {
        return SpriteAccessor.init((class_2960)registry.get(this.atlasId), this.contents.export(registry), this.atlasWidth, this.atlasHeight, this.x, this.y);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashSprite that = (DashSprite)o;
        if (this.atlasId != that.atlasId) {
            return false;
        }
        if (this.x != that.x) {
            return false;
        }
        if (this.y != that.y) {
            return false;
        }
        if (this.atlasWidth != that.atlasWidth) {
            return false;
        }
        if (this.atlasHeight != that.atlasHeight) {
            return false;
        }
        return this.contents.equals(that.contents);
    }

    public int hashCode() {
        int result = this.atlasId;
        result = 31 * result + this.contents.hashCode();
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.atlasWidth;
        result = 31 * result + this.atlasHeight;
        return result;
    }
}

