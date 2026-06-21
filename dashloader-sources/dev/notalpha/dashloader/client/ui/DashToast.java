/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1041
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_287$class_7433
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_368
 *  net.minecraft.class_368$class_369
 *  net.minecraft.class_374
 *  net.minecraft.class_4587
 *  net.minecraft.class_757
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector4f
 */
package dev.notalpha.dashloader.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.notalpha.dashloader.client.ui.Color;
import dev.notalpha.dashloader.client.ui.DashToastState;
import dev.notalpha.dashloader.client.ui.DashToastStatus;
import dev.notalpha.dashloader.client.ui.DrawerUtil;
import dev.notalpha.dashloader.misc.HahaManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;
import net.minecraft.class_1041;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_368;
import net.minecraft.class_374;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;

public class DashToast
implements class_368 {
    private static final int PROGRESS_BAR_HEIGHT = 2;
    private static final int PADDING = 8;
    private static final int LINES = 125;
    private final Random random = new Random();
    private List<Line> lines = new ArrayList<Line>();
    @Nullable
    private final String fact = HahaManager.getFact();
    private long oldTime = System.currentTimeMillis();
    public final DashToastState state = new DashToastState();

    private static void drawVertex(Matrix4f m4f, class_287 bb, float z, float x, float y, Color color) {
        bb.method_22918(m4f, x, y, z).method_1336(color.red(), color.green(), color.blue(), color.alpha()).method_1344();
    }

    public int method_29049() {
        return 200;
    }

    public int method_29050() {
        return 40;
    }

    public DashToast() {
        for (int i = 0; i < 125; ++i) {
            this.lines.add(new Line());
        }
    }

    public class_368.class_369 method_1986(class_332 context, class_374 manager, long startTime) {
        int n;
        Color progressColor;
        float progress;
        int width = this.method_29049();
        int height = this.method_29050();
        int barY = height - 2;
        if (this.state.getStatus() == DashToastStatus.CRASHED) {
            progress = (float)this.state.getProgress();
            progressColor = DrawerUtil.FAILED_COLOR;
        } else {
            progress = (float)this.state.getProgress();
            progressColor = DrawerUtil.getProgressColor(progress);
        }
        ArrayList<Line> newList = new ArrayList<Line>();
        ArrayList<Line> newListPrio = new ArrayList<Line>();
        long currentTime = System.currentTimeMillis();
        for (Line line : this.lines) {
            if (line.tick(width, height, progress, (float)(currentTime - this.oldTime) / 17.0f)) {
                newListPrio.add(line);
                continue;
            }
            newList.add(line);
        }
        this.oldTime = currentTime;
        this.lines = newList;
        this.lines.addAll(newListPrio);
        class_4587 matrices = context.method_51448();
        Vector4f vec = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        vec.mul((Matrix4fc)matrices.method_23760().method_23761());
        class_1041 window = manager.method_1995().method_22683();
        double scale = window.method_4495();
        RenderSystem.enableScissor((int)((int)((double)vec.x * scale)), (int)((int)((double)window.method_4506() - (double)vec.y * scale - (double)height * scale)), (int)((int)((double)width * scale)), (int)((int)((double)height * scale)));
        DrawerUtil.drawRect(context, 0, 0, width, height, DrawerUtil.BACKGROUND_COLOR);
        this.drawBatched(matrices, (matrix4f, bufferBuilder) -> {
            for (Line line : this.lines) {
                line.draw((Matrix4f)matrix4f, (class_287)bufferBuilder);
            }
        });
        class_327 textRenderer = manager.method_1995().field_1772;
        String progressText = this.state.getProgressText();
        if (this.fact != null) {
            n = barY - 8;
        } else {
            int n2 = barY / 2;
            Objects.requireNonNull(textRenderer);
            n = n2 + 9 / 2;
        }
        int progressTextY = n;
        DrawerUtil.drawText(context, textRenderer, 8, progressTextY, this.state.getText(), DrawerUtil.STATUS_COLOR);
        DrawerUtil.drawText(context, textRenderer, width - 8 - textRenderer.method_1727(progressText), progressTextY, progressText, DrawerUtil.STATUS_COLOR);
        if (this.fact != null) {
            Objects.requireNonNull(textRenderer);
            DrawerUtil.drawText(context, textRenderer, 8, 9 + 8, this.fact, DrawerUtil.FOREGROUND_COLOR);
        }
        DrawerUtil.drawRect(context, 0, barY, width, 2, DrawerUtil.PROGRESS_TRACK);
        DrawerUtil.drawRect(context, 0, barY, (int)((float)width * progress), 2, progressColor);
        this.drawBatched(matrices, (matrix4f, bb) -> {
            for (Line line : this.lines) {
                line.drawGlow((Matrix4f)matrix4f, (class_287)bb);
            }
            DrawerUtil.drawGlow(matrix4f, bb, 0.0f, barY, (int)((float)width * progress), 2.0f, 0.75f, progressColor, true, true, true, true);
        });
        RenderSystem.disableScissor();
        if (this.state.getStatus() == DashToastStatus.CRASHED && System.currentTimeMillis() - this.state.getTimeDone() > 10000L) {
            return class_368.class_369.field_2209;
        }
        if (this.state.getStatus() == DashToastStatus.DONE && System.currentTimeMillis() - this.state.getTimeDone() > 2000L) {
            return class_368.class_369.field_2209;
        }
        return class_368.class_369.field_2210;
    }

    private void drawBatched(class_4587 ms, BiConsumer<Matrix4f, class_287> consumer) {
        class_287 bufferBuilder = class_289.method_1348().method_1349();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(class_757::method_34540);
        bufferBuilder.method_1328(class_293.class_5596.field_27382, class_290.field_1576);
        Matrix4f matrix = ms.method_23760().method_23761();
        consumer.accept(matrix, bufferBuilder);
        class_286.method_43433((class_287.class_7433)bufferBuilder.method_1326());
        RenderSystem.disableBlend();
    }

    private final class Line {
        public ColorKind colorKind;
        public float x = -1000.0f;
        public float y = -1000.0f;
        public int width;
        public int height;
        public float speedBoost;
        private Color color;

        public Line() {
            this.width = DashToast.this.random.nextInt(30, 50);
            this.height = DashToast.this.random.nextInt(2, 5);
            this.colorKind = ColorKind.Neutral;
            this.color = new Color(-16776961);
        }

        public boolean tick(int screenWidth, int screenHeight, float progress, float delta) {
            this.x += (float)((double)this.speedBoost * (0.8 + 2.5 * (double)progress)) * delta;
            if (this.x > (float)screenWidth || this.x + (float)this.width < 0.0f) {
                this.x = -this.width;
                this.y = (float)screenHeight * DashToast.this.random.nextFloat();
                if (DashToast.this.state.getStatus() == DashToastStatus.CRASHED) {
                    if ((double)DashToast.this.random.nextFloat() > 0.9 || this.colorKind == ColorKind.Progress) {
                        this.colorKind = ColorKind.Crashed;
                    }
                } else {
                    this.colorKind = (double)DashToast.this.random.nextFloat() > 0.95 ? ColorKind.Progress : ColorKind.Neutral;
                }
                float weight = 1.0f - this.getWeight();
                float weightSpeed = (float)(0.7 + (double)weight * 0.6);
                float kindSpeed = this.colorKind == ColorKind.Neutral ? (float)(1.0 + (double)(DashToast.this.random.nextFloat() * 0.2f)) : (float)(1.0 + (double)(DashToast.this.random.nextFloat() * 0.8f));
                this.speedBoost = kindSpeed * weightSpeed;
                return this.colorKind != ColorKind.Neutral;
            }
            this.color = this.getColor(progress);
            return false;
        }

        public void draw(Matrix4f b4, class_287 bb) {
            Color end = DrawerUtil.withOpacity(this.color, 0.0f);
            DashToast.drawVertex(b4, bb, 0.0f, this.x + (float)this.width, this.y, this.color);
            DashToast.drawVertex(b4, bb, 0.0f, this.x, this.y, end);
            DashToast.drawVertex(b4, bb, 0.0f, this.x, this.y + (float)this.height, end);
            DashToast.drawVertex(b4, bb, 0.0f, this.x + (float)this.width, this.y + (float)this.height, this.color);
        }

        public void drawGlow(Matrix4f b4, class_287 bb) {
            if (this.colorKind != ColorKind.Neutral) {
                DrawerUtil.drawGlow(b4, bb, this.x, this.y, this.width, this.height, (this.getWeight() + 2.0f) / 3.0f, this.color, false, true, false, true);
            }
        }

        public Color getColor(double progress) {
            Color color = switch (this.colorKind) {
                default -> throw new IncompatibleClassChangeError();
                case ColorKind.Neutral -> DrawerUtil.NEUTRAL_LINE;
                case ColorKind.Progress -> {
                    if (DashToast.this.state.getStatus() == DashToastStatus.CRASHED) {
                        yield DrawerUtil.FAILED_COLOR;
                    }
                    yield DrawerUtil.getProgressColor(progress);
                }
                case ColorKind.Crashed -> DrawerUtil.FAILED_COLOR;
            };
            return DrawerUtil.withOpacity(color, class_3532.method_15363((float)(this.x / (float)this.width), (float)0.0f, (float)1.0f));
        }

        public float getWeight() {
            return ((float)this.width * (float)this.height - 60.0f) / 190.0f;
        }
    }

    public static enum ColorKind {
        Neutral,
        Progress,
        Crashed;

    }
}

