/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;

public final class UnsafeHelper {
    public static final Unsafe UNSAFE = UnsafeHelper.getUnsafeInstance();

    private static Unsafe getUnsafeInstance() {
        Class<Unsafe> clazz = Unsafe.class;
        for (Field field : clazz.getDeclaredFields()) {
            int modifiers;
            if (!field.getType().equals(clazz) || !Modifier.isStatic(modifiers = field.getModifiers()) || !Modifier.isFinal(modifiers)) continue;
            try {
                field.setAccessible(true);
                return (Unsafe)field.get(null);
            }
            catch (Exception exception) {
                break;
            }
        }
        throw new IllegalStateException("Unsafe is unavailable.");
    }

    public static <O> O allocateInstance(Class<O> closs) {
        try {
            return (O)UNSAFE.allocateInstance(closs);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}

