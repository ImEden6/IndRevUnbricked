/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.DataNullable
 *  net.minecraft.class_804
 *  net.minecraft.class_809
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.client.model.components;

import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import java.util.Objects;
import net.minecraft.class_804;
import net.minecraft.class_809;
import org.jetbrains.annotations.Nullable;

@DataNullable
public final class DashModelTransformation {
    public final class_804 thirdPersonLeftHand;
    public final class_804 thirdPersonRightHand;
    public final class_804 firstPersonLeftHand;
    public final class_804 firstPersonRightHand;
    public final class_804 head;
    public final class_804 gui;
    public final class_804 ground;
    public final class_804 fixed;
    public transient int nullTransformations = 0;

    public DashModelTransformation(@Nullable class_804 thirdPersonLeftHand, @Nullable class_804 thirdPersonRightHand, @Nullable class_804 firstPersonLeftHand, @Nullable class_804 firstPersonRightHand, @Nullable class_804 head, @Nullable class_804 gui, @Nullable class_804 ground, @Nullable class_804 fixed) {
        this.thirdPersonLeftHand = thirdPersonLeftHand;
        this.thirdPersonRightHand = thirdPersonRightHand;
        this.firstPersonLeftHand = firstPersonLeftHand;
        this.firstPersonRightHand = firstPersonRightHand;
        this.head = head;
        this.gui = gui;
        this.ground = ground;
        this.fixed = fixed;
    }

    public DashModelTransformation(class_809 other) {
        this.thirdPersonLeftHand = this.createTransformation(other.field_4305);
        this.thirdPersonRightHand = this.createTransformation(other.field_4307);
        this.firstPersonLeftHand = this.createTransformation(other.field_4302);
        this.firstPersonRightHand = this.createTransformation(other.field_4304);
        this.head = this.createTransformation(other.field_4311);
        this.gui = this.createTransformation(other.field_4300);
        this.ground = this.createTransformation(other.field_4303);
        this.fixed = this.createTransformation(other.field_4306);
    }

    @Nullable
    public static DashModelTransformation createDashOrReturnNullIfDefault(class_809 other) {
        if (other == class_809.field_4301) {
            return null;
        }
        DashModelTransformation out = new DashModelTransformation(other);
        if (out.nullTransformations == 8) {
            return null;
        }
        return out;
    }

    public static class_809 exportOrDefault(@Nullable DashModelTransformation other) {
        if (other == null) {
            return class_809.field_4301;
        }
        return other.export();
    }

    private class_804 createTransformation(class_804 transformation) {
        if (transformation == class_804.field_4284) {
            ++this.nullTransformations;
            return null;
        }
        return transformation;
    }

    private class_804 unTransformation(class_804 transformation) {
        return transformation == null ? class_804.field_4284 : transformation;
    }

    public class_809 export() {
        return new class_809(this.unTransformation(this.thirdPersonLeftHand), this.unTransformation(this.thirdPersonRightHand), this.unTransformation(this.firstPersonLeftHand), this.unTransformation(this.firstPersonRightHand), this.unTransformation(this.head), this.unTransformation(this.gui), this.unTransformation(this.ground), this.unTransformation(this.fixed));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashModelTransformation that = (DashModelTransformation)o;
        if (!Objects.equals(this.thirdPersonLeftHand, that.thirdPersonLeftHand)) {
            return false;
        }
        if (!Objects.equals(this.thirdPersonRightHand, that.thirdPersonRightHand)) {
            return false;
        }
        if (!Objects.equals(this.firstPersonLeftHand, that.firstPersonLeftHand)) {
            return false;
        }
        if (!Objects.equals(this.firstPersonRightHand, that.firstPersonRightHand)) {
            return false;
        }
        if (!Objects.equals(this.head, that.head)) {
            return false;
        }
        if (!Objects.equals(this.gui, that.gui)) {
            return false;
        }
        if (!Objects.equals(this.ground, that.ground)) {
            return false;
        }
        return Objects.equals(this.fixed, that.fixed);
    }

    public int hashCode() {
        int result = this.thirdPersonLeftHand != null ? this.thirdPersonLeftHand.hashCode() : 0;
        result = 31 * result + (this.thirdPersonRightHand != null ? this.thirdPersonRightHand.hashCode() : 0);
        result = 31 * result + (this.firstPersonLeftHand != null ? this.firstPersonLeftHand.hashCode() : 0);
        result = 31 * result + (this.firstPersonRightHand != null ? this.firstPersonRightHand.hashCode() : 0);
        result = 31 * result + (this.head != null ? this.head.hashCode() : 0);
        result = 31 * result + (this.gui != null ? this.gui.hashCode() : 0);
        result = 31 * result + (this.ground != null ? this.ground.hashCode() : 0);
        result = 31 * result + (this.fixed != null ? this.fixed.hashCode() : 0);
        return result;
    }
}

