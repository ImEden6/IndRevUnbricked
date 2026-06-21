/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_284
 *  net.minecraft.class_3679
 *  net.minecraft.class_5944
 */
package dev.notalpha.dashloader.client.shader;

import dev.notalpha.dashloader.io.IOHelper;
import dev.notalpha.dashloader.mixin.accessor.GlUniformAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import net.minecraft.class_284;
import net.minecraft.class_3679;
import net.minecraft.class_5944;

public final class DashGlUniform {
    public final int dataType;
    public final boolean loaded;
    public final String name;
    public final int @DataNullable [] intData;
    public final float @DataNullable [] floatData;

    public DashGlUniform(int dataType, boolean loaded, String name, int[] intData, float[] floatData) {
        this.dataType = dataType;
        this.loaded = loaded;
        this.name = name;
        this.intData = intData;
        this.floatData = floatData;
    }

    public DashGlUniform(class_284 glUniform, boolean loaded) {
        GlUniformAccessor access = (GlUniformAccessor)glUniform;
        this.intData = IOHelper.toArray(access.getIntData());
        this.floatData = IOHelper.toArray(access.getFloatData());
        this.dataType = glUniform.method_35662();
        this.name = glUniform.method_1298();
        this.loaded = loaded;
    }

    public class_284 export(class_5944 shader) {
        class_284 glUniform = new class_284(this.name, this.dataType, 0, (class_3679)shader);
        GlUniformAccessor access = (GlUniformAccessor)glUniform;
        access.setIntData(IOHelper.fromArray(this.intData));
        access.setFloatData(IOHelper.fromArray(this.floatData));
        return glUniform;
    }
}

