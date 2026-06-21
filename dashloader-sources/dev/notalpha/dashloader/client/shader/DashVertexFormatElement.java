/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_290
 *  net.minecraft.class_296
 *  net.minecraft.class_296$class_297
 *  net.minecraft.class_296$class_298
 */
package dev.notalpha.dashloader.client.shader;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_290;
import net.minecraft.class_296;

public class DashVertexFormatElement
implements DashObject<class_296> {
    public static final List<class_296> BUILT_IN = new ArrayList<class_296>();
    public final @DataNullable DashVertexFormatElementData data;
    public final int builtin;

    public DashVertexFormatElement(@DataNullable DashVertexFormatElementData data, int builtin) {
        this.data = data;
        this.builtin = builtin;
    }

    public DashVertexFormatElement(class_296 element) {
        int builtin = -1;
        for (int i = 0; i < BUILT_IN.size(); ++i) {
            if (BUILT_IN.get(i) != element) continue;
            builtin = i;
            break;
        }
        this.data = builtin == -1 ? new DashVertexFormatElementData(element) : null;
        this.builtin = builtin;
    }

    @Override
    public class_296 export(RegistryReader reader) {
        if (this.builtin != -1) {
            return BUILT_IN.get(this.builtin);
        }
        return new class_296(this.data.uvIndex, this.data.componentType, this.data.type, this.data.componentCount);
    }

    static {
        BUILT_IN.add(class_290.field_1587);
        BUILT_IN.add(class_290.field_1581);
        BUILT_IN.add(class_290.field_1591);
        BUILT_IN.add(class_290.field_1583);
        BUILT_IN.add(class_290.field_20886);
        BUILT_IN.add(class_290.field_1579);
        BUILT_IN.add(class_290.field_1578);
        BUILT_IN.add(class_290.field_29335);
    }

    public static class DashVertexFormatElementData {
        public final class_296.class_297 componentType;
        public final class_296.class_298 type;
        public final int uvIndex;
        public final int componentCount;

        public DashVertexFormatElementData(class_296.class_297 componentType, class_296.class_298 type, int uvIndex, int componentCount) {
            this.componentType = componentType;
            this.type = type;
            this.uvIndex = uvIndex;
            this.componentCount = componentCount;
        }

        public DashVertexFormatElementData(class_296 element) {
            this.componentType = element.method_1386();
            this.type = element.method_1382();
            this.uvIndex = element.method_1385();
            this.componentCount = element.method_34451();
        }
    }
}

