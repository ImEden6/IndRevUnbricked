/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
package dev.notalpha.dashloader.client.blockstate;

import com.google.common.collect.ImmutableList;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.mixin.accessor.ModelLoaderAccessor;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class DashBlockState
implements DashObject<class_2680> {
    public static final class_2960 ITEM_FRAME = new class_2960("dashloader:itemframewhy");
    public final int owner;
    public final int pos;

    public DashBlockState(int owner, int pos) {
        this.owner = owner;
        this.pos = pos;
    }

    public DashBlockState(class_2680 blockState, RegistryWriter writer) {
        class_2680 state;
        int i;
        class_2248 block = blockState.method_26204();
        int pos = -1;
        class_2960 owner = null;
        ImmutableList states = ModelLoaderAccessor.getTheItemFrameThing().method_11662();
        for (i = 0; i < states.size(); ++i) {
            state = (class_2680)states.get(i);
            if (!state.equals(blockState)) continue;
            pos = i;
            owner = ITEM_FRAME;
            break;
        }
        if (pos == -1) {
            states = block.method_9595().method_11662();
            for (i = 0; i < states.size(); ++i) {
                state = (class_2680)states.get(i);
                if (!state.equals(blockState)) continue;
                pos = i;
                owner = class_7923.field_41175.method_10221((Object)block);
                break;
            }
        }
        if (owner == null) {
            throw new RuntimeException("Could not find a blockstate for " + blockState);
        }
        this.owner = writer.add(owner);
        this.pos = pos;
    }

    @Override
    public class_2680 export(RegistryReader reader) {
        class_2960 id = (class_2960)reader.get(this.owner);
        if (id.equals((Object)ITEM_FRAME)) {
            return (class_2680)ModelLoaderAccessor.getTheItemFrameThing().method_11662().get(this.pos);
        }
        return (class_2680)((class_2248)class_7923.field_41175.method_10223(id)).method_9595().method_11662().get(this.pos);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashBlockState that = (DashBlockState)o;
        if (this.owner != that.owner) {
            return false;
        }
        return this.pos == that.pos;
    }

    public int hashCode() {
        int result = this.owner;
        result = 31 * result + this.pos;
        return result;
    }
}

