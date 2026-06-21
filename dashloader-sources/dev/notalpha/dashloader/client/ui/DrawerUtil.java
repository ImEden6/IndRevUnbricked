/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_287
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  org.joml.Matrix4f
 */
package dev.notalpha.dashloader.client.ui;

import dev.notalpha.dashloader.client.ui.Color;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_287;
import net.minecraft.class_327;
import net.minecraft.class_332;
import org.joml.Matrix4f;

public class DrawerUtil {
    public static final float GLOW_SIZE = 30.0f;
    public static final float GLOW_STRENGTH = 0.1f;
    public static final Color FAILED_COLOR = new Color(250, 68, 51);
    public static final Color BACKGROUND_COLOR = new Color(34, 31, 34);
    public static final Color FOREGROUND_COLOR = new Color(252, 252, 250);
    public static final Color STATUS_COLOR = new Color(180, 180, 180);
    public static final Color NEUTRAL_LINE = new Color(45, 42, 46);
    public static final Color PROGRESS_TRACK = new Color(25, 25, 25);
    private static final Color[] PROGRESS_COLORS = new Color[]{new Color(255, 97, 136), new Color(252, 152, 103), new Color(255, 216, 102), new Color(169, 220, 118)};

    public static void drawRect(class_332 context, int x, int y, int width, int height, Color color) {
        int x2 = width + x;
        int y2 = height + y;
        context.method_25294(x, y, x2, y2, color.argb());
    }

    public static void drawText(class_332 context, class_327 textRenderer, int x, int y, String text, Color color) {
        class_2561 class_25612 = class_2561.method_30163((String)text);
        Objects.requireNonNull(textRenderer);
        context.method_27535(textRenderer, class_25612, x, y - 9, color.argb());
    }

    private static void drawVertex(Matrix4f m4f, class_287 bb, float x, float y, Color color) {
        bb.method_22918(m4f, x, y, 0.0f).method_1336(color.red(), color.green(), color.blue(), color.alpha()).method_1344();
    }

    public static void drawGlow(Matrix4f b4, class_287 bb, float x, float y, float width, float height, float strength, Color color, boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        Color end = DrawerUtil.withOpacity(color, 0.0f);
        Color glow = DrawerUtil.withOpacity(color, 0.1f * strength);
        Color tl = topLeft ? glow : end;
        Color tr = topRight ? glow : end;
        Color bl = bottomLeft ? glow : end;
        Color br = bottomRight ? glow : end;
        Color tlEnd = new Color(tl.red(), tl.green(), tl.blue(), 0);
        Color trEnd = new Color(tr.red(), tr.green(), tr.blue(), 0);
        Color blEnd = new Color(bl.red(), bl.green(), bl.blue(), 0);
        Color brEnd = new Color(br.red(), br.green(), br.blue(), 0);
        float x2 = x + width;
        float y2 = y + height;
        DrawerUtil.drawVertex(b4, bb, x, y2, bl);
        DrawerUtil.drawVertex(b4, bb, x2, y2, br);
        DrawerUtil.drawVertex(b4, bb, x2, y, tr);
        DrawerUtil.drawVertex(b4, bb, x, y, tl);
        DrawerUtil.drawVertex(b4, bb, x, y, tl);
        DrawerUtil.drawVertex(b4, bb, x2, y, tr);
        DrawerUtil.drawVertex(b4, bb, x2, y - 30.0f, trEnd);
        DrawerUtil.drawVertex(b4, bb, x, y - 30.0f, tlEnd);
        DrawerUtil.drawVertex(b4, bb, x2, y - 30.0f, trEnd);
        DrawerUtil.drawVertex(b4, bb, x2, y, tr);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y, trEnd);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y - 30.0f, trEnd);
        DrawerUtil.drawVertex(b4, bb, x, y - 30.0f, tlEnd);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y - 30.0f, tlEnd);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y, tlEnd);
        DrawerUtil.drawVertex(b4, bb, x, y, tl);
        DrawerUtil.drawVertex(b4, bb, x2, y2 + 30.0f, brEnd);
        DrawerUtil.drawVertex(b4, bb, x2, y2, br);
        DrawerUtil.drawVertex(b4, bb, x, y2, bl);
        DrawerUtil.drawVertex(b4, bb, x, y2 + 30.0f, blEnd);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y2, brEnd);
        DrawerUtil.drawVertex(b4, bb, x2, y2, br);
        DrawerUtil.drawVertex(b4, bb, x2, y2 + 30.0f, brEnd);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y2 + 30.0f, brEnd);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y2, blEnd);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y2 + 30.0f, blEnd);
        DrawerUtil.drawVertex(b4, bb, x, y2 + 30.0f, blEnd);
        DrawerUtil.drawVertex(b4, bb, x, y2, bl);
        DrawerUtil.drawVertex(b4, bb, x2, y, tr);
        DrawerUtil.drawVertex(b4, bb, x2, y2, br);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y2, brEnd);
        DrawerUtil.drawVertex(b4, bb, x2 + 30.0f, y, trEnd);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y2, blEnd);
        DrawerUtil.drawVertex(b4, bb, x, y2, bl);
        DrawerUtil.drawVertex(b4, bb, x, y, tl);
        DrawerUtil.drawVertex(b4, bb, x - 30.0f, y, tlEnd);
    }

    public static int convertColor(Color color) {
        return color.rgb() | color.alpha() << 24;
    }

    public static Color withOpacity(Color color, float opacity) {
        float currentOpacity = (float)color.alpha() / 255.0f;
        return new Color(color.red(), color.green(), color.blue(), (int)(opacity * currentOpacity * 255.0f));
    }

    public static Color getProgressColor(double progress) {
        return DrawerUtil.mix(progress, PROGRESS_COLORS);
    }

    private static Color mix(double pos, Color ... colors) {
        if (colors.length == 1) {
            return colors[0];
        }
        pos = Math.min(1.0, Math.max(0.0, pos));
        int breaks = colors.length - 1;
        if (pos == 1.0) {
            return colors[breaks];
        }
        int colorPos = (int)Math.floor(pos * (double)breaks);
        double step = 1.0 / (double)breaks;
        double localRatio = pos % step * (double)breaks;
        return DrawerUtil.blend(colors[colorPos], colors[colorPos + 1], localRatio);
    }

    private static Color blend(Color i1, Color i2, double ratio) {
        if (ratio > 1.0) {
            ratio = 1.0;
        } else if (ratio < 0.0) {
            ratio = 0.0;
        }
        double iRatio = 1.0 - ratio;
        int a = (int)((double)i1.alpha() * iRatio + (double)i2.alpha() * ratio);
        int r = (int)((double)i1.red() * iRatio + (double)i2.red() * ratio);
        int g = (int)((double)i1.green() * iRatio + (double)i2.green() * ratio);
        int b = (int)((double)i1.blue() * iRatio + (double)i2.blue() * ratio);
        return new Color(r, g, b, a);
    }
}

