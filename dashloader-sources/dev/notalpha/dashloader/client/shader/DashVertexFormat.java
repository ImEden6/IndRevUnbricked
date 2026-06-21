/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_290
 *  net.minecraft.class_293
 *  net.minecraft.class_296
 */
package dev.notalpha.dashloader.client.shader;

import com.google.common.collect.ImmutableMap;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.client.shader.DashVertexFormatElement;
import dev.notalpha.dashloader.mixin.accessor.VertexFormatAccessor;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_296;

public class DashVertexFormat
implements DashObject<class_293> {
    public static final List<class_293> BUILT_IN = new ArrayList<class_293>();
    public final @DataNullable Map<String, DashVertexFormatElement> elementMap;
    public final int builtin;

    public DashVertexFormat(Map<String, DashVertexFormatElement> elementMap, int builtin) {
        this.elementMap = elementMap;
        this.builtin = builtin;
    }

    public DashVertexFormat(class_293 vertexFormat) {
        int builtin = -1;
        for (int i = 0; i < BUILT_IN.size(); ++i) {
            class_293 format = BUILT_IN.get(i);
            if (format != vertexFormat) continue;
            builtin = i;
            break;
        }
        this.builtin = builtin;
        if (builtin == -1) {
            this.elementMap = new HashMap<String, DashVertexFormatElement>();
            ((VertexFormatAccessor)vertexFormat).getElementMap().forEach((s, element) -> this.elementMap.put((String)s, new DashVertexFormatElement((class_296)element)));
        } else {
            this.elementMap = null;
        }
    }

    @Override
    public class_293 export(RegistryReader reader) {
        if (this.builtin != -1) {
            return BUILT_IN.get(this.builtin);
        }
        ImmutableMap.Builder out = ImmutableMap.builderWithExpectedSize((int)this.elementMap.size());
        this.elementMap.forEach((s, dashVertexFormatElement) -> {
            class_296 export = dashVertexFormatElement.export(reader);
            out.put(s, (Object)export);
        });
        return new class_293(out.build());
    }

    static {
        BUILT_IN.add(class_290.field_29336);
        BUILT_IN.add(class_290.field_1590);
        BUILT_IN.add(class_290.field_1580);
        BUILT_IN.add(class_290.field_1584);
        BUILT_IN.add(class_290.field_1592);
        BUILT_IN.add(class_290.field_1576);
        BUILT_IN.add(class_290.field_29337);
        BUILT_IN.add(class_290.field_21468);
        BUILT_IN.add(class_290.field_1585);
        BUILT_IN.add(class_290.field_20887);
        BUILT_IN.add(class_290.field_1575);
        BUILT_IN.add(class_290.field_20888);
        BUILT_IN.add(class_290.field_1586);
        BUILT_IN.add(class_290.field_1577);
    }
}

