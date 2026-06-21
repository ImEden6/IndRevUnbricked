/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
 */
package dev.notalpha.dashloader.client.model.components;

import dev.notalpha.dashloader.misc.UnsafeHelper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;

public final class DashMesh {
    public static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap();
    public final int[] data;
    public final String className;

    public DashMesh(int[] data, String className) {
        this.data = data;
        this.className = className;
    }

    public DashMesh(Mesh mesh) {
        this(DashMesh.getData(mesh), mesh.getClass().getName());
    }

    private static int[] getData(Mesh mesh) {
        int[] data;
        try {
            Field field = mesh.getClass().getDeclaredField("data");
            field.setAccessible(true);
            data = (int[])field.get(mesh);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Could not use Mesh field hack. ", e);
        }
        return data;
    }

    public Mesh export() {
        Class<?> aClass = DashMesh.getClass(this.className);
        Mesh mesh = (Mesh)UnsafeHelper.allocateInstance(aClass);
        try {
            assert (aClass != null);
            Field data = aClass.getDeclaredField("data");
            data.setAccessible(true);
            data.set(mesh, this.data);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Could not use Mesh field hack. ", e);
        }
        return mesh;
    }

    public static Class<?> getClass(String className) {
        Class<?> closs = CLASS_CACHE.get(className);
        if (closs != null) {
            return closs;
        }
        try {
            Class<?> clz = Class.forName(className);
            CLASS_CACHE.put(className, clz);
            return clz;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashMesh dashMesh = (DashMesh)o;
        if (!Arrays.equals(this.data, dashMesh.data)) {
            return false;
        }
        return this.className.equals(dashMesh.className);
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.data);
        result = 31 * result + this.className.hashCode();
        return result;
    }
}

