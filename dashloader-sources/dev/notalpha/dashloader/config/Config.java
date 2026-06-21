/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public Map<String, Boolean> options = new LinkedHashMap<String, Boolean>();
    public byte compression = (byte)3;
    public int maxCaches = 5;
    public List<String> customSplashLines = new ArrayList<String>();
    public boolean addDefaultSplashLines = true;
    public boolean singleThreadedReading = false;
    public boolean showCachingToast = true;
}

