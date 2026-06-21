/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.misc;

public final class ProfilerUtil {
    public static long RELOAD_START = 0L;

    public static String getTimeStringFromStart(long start) {
        return ProfilerUtil.getTimeString(System.currentTimeMillis() - start);
    }

    public static String getTimeString(long ms) {
        if (ms >= 60000L) {
            return (int)(ms / 60000L) + "m " + (int)(ms % 60000L) / 1000 + "s";
        }
        if (ms >= 3000L) {
            return ProfilerUtil.printMsToSec(ms) + "s";
        }
        return ms + "ms";
    }

    private static float printMsToSec(long ms) {
        return (float)Math.round((float)ms / 100.0f) / 10.0f;
    }
}

