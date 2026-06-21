/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.notalpha.taski.ParentTask
 *  dev.notalpha.taski.Task
 *  dev.notalpha.taski.builtin.AbstractTask
 *  dev.notalpha.taski.builtin.StaticTask
 *  net.minecraft.class_2477
 *  net.minecraft.class_310
 */
package dev.notalpha.dashloader.client.ui;

import dev.notalpha.dashloader.DashLoader;
import dev.notalpha.dashloader.client.ui.DashToastStatus;
import dev.notalpha.taski.ParentTask;
import dev.notalpha.taski.Task;
import dev.notalpha.taski.builtin.AbstractTask;
import dev.notalpha.taski.builtin.StaticTask;
import java.io.InputStream;
import java.util.HashMap;
import net.minecraft.class_2477;
import net.minecraft.class_310;

public final class DashToastState {
    public Task task = new StaticTask("Idle", 0.0f);
    private final HashMap<String, String> translations;
    private String overwriteText;
    private DashToastStatus status;
    private double currentProgress = 0.0;
    private long lastUpdate = System.currentTimeMillis();
    private long timeDone = System.currentTimeMillis();

    public DashToastState() {
        String langCode = class_310.method_1551().method_1526().method_4669();
        DashLoader.LOG.info(langCode);
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("dashloader/lang/" + langCode + ".json");
        this.translations = new HashMap();
        if (stream != null) {
            DashLoader.LOG.info("Found translations");
            class_2477.method_29425((InputStream)stream, this.translations::put);
        } else {
            InputStream en_stream = this.getClass().getClassLoader().getResourceAsStream("dashloader/lang/en_us.json");
            if (en_stream != null) {
                class_2477.method_29425((InputStream)en_stream, this.translations::put);
            }
        }
    }

    private void tickProgress() {
        double actualProgress;
        if (Double.isNaN(this.currentProgress)) {
            this.currentProgress = 0.0;
        }
        double divisionSpeed = (actualProgress = (double)this.task.getProgress()) < this.currentProgress ? 3.0 : 30.0;
        double currentProgress1 = (actualProgress - this.currentProgress) / divisionSpeed;
        this.currentProgress += currentProgress1;
    }

    public double getProgress() {
        long currentTime = System.currentTimeMillis();
        while (currentTime > this.lastUpdate) {
            this.tickProgress();
            this.lastUpdate += 10L;
        }
        return this.currentProgress;
    }

    public String getText() {
        if (this.overwriteText != null) {
            return this.overwriteText;
        }
        String text = this.concatTask(3, this.task);
        return this.translations.getOrDefault(text, text);
    }

    public String getProgressText() {
        return this.getProgressText(3, this.task);
    }

    private String concatTask(int depth, Task task) {
        String name = null;
        if (task instanceof AbstractTask) {
            AbstractTask abstractTask = (AbstractTask)task;
            name = abstractTask.getName();
        }
        if (task instanceof ParentTask) {
            String subName;
            ParentTask stepTask = (ParentTask)task;
            Task subTask = stepTask.getChild();
            if (depth > 1 && (subName = this.concatTask(depth - 1, subTask)) != null) {
                return name + "." + subName;
            }
        }
        return name;
    }

    private String getProgressText(int depth, Task task) {
        if (task instanceof ParentTask) {
            String subName;
            ParentTask stepTask = (ParentTask)task;
            Task subTask = stepTask.getChild();
            if (depth > 1 && (subName = this.getProgressText(depth - 1, subTask)) != null) {
                return subName;
            }
        }
        if (task instanceof AbstractTask) {
            AbstractTask abstractTask = (AbstractTask)task;
            return abstractTask.getProgressText();
        }
        return null;
    }

    public void setOverwriteText(String overwriteText) {
        this.overwriteText = this.translations.getOrDefault(overwriteText, overwriteText);
    }

    public DashToastStatus getStatus() {
        return this.status;
    }

    public void setStatus(DashToastStatus status) {
        this.status = status;
    }

    public long getTimeDone() {
        return this.timeDone;
    }

    public void setDone() {
        this.timeDone = System.currentTimeMillis();
    }
}

