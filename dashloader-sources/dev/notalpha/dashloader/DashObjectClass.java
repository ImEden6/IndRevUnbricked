/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.util.ScanUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader;

import dev.notalpha.dashloader.api.DashObject;
import dev.quantumfusion.hyphen.util.ScanUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DashObjectClass<R, D extends DashObject<R>> {
    private final Class<D> dashClass;
    @Nullable
    private Class<R> targetClass;
    int dashObjectId;

    public DashObjectClass(Class<?> dashClass) {
        this.dashClass = dashClass;
    }

    public Class<D> getDashClass() {
        return this.dashClass;
    }

    @NotNull
    public Class<R> getTargetClass() {
        if (this.targetClass == null) {
            Type[] genericInterfaces = this.dashClass.getGenericInterfaces();
            if (genericInterfaces.length == 0) {
                throw new RuntimeException(this.dashClass + " does not implement DashObject.");
            }
            boolean foundDashObject = false;
            for (Type genericInterface : genericInterfaces) {
                if (ScanUtil.getClassFrom((Type)genericInterface) != DashObject.class) continue;
                foundDashObject = true;
                if (genericInterface instanceof ParameterizedType) {
                    ParameterizedType targetClass = (ParameterizedType)genericInterface;
                    Type[] actualTypeArguments = targetClass.getActualTypeArguments();
                    Class classFrom = ScanUtil.getClassFrom((Type)actualTypeArguments[0]);
                    if (classFrom == null) {
                        throw new RuntimeException(this.dashClass + " has a non resolvable DashObject parameter");
                    }
                    this.targetClass = classFrom;
                    continue;
                }
                throw new RuntimeException(this.dashClass + " implements raw DashObject");
            }
            if (!foundDashObject) {
                throw new RuntimeException(this.dashClass + " must implement DashObject");
            }
        }
        return this.targetClass;
    }

    public int getDashObjectId() {
        return this.dashObjectId;
    }

    public String toString() {
        return "DashObjectClass{dashClass=" + this.dashClass + ", targetClass=" + this.targetClass + ", dashObjectId=" + this.dashObjectId + "}";
    }
}

