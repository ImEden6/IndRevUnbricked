/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_815
 */
package dev.notalpha.dashloader.client.model.predicates;

import java.util.function.Predicate;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_815;

public class BooleanSelector
implements class_815 {
    public final boolean selector;

    public BooleanSelector(boolean selector) {
        this.selector = selector;
    }

    public BooleanSelector(class_815 selector) {
        this.selector = selector == class_815.field_16900;
    }

    public Predicate<class_2680> getPredicate(class_2689<class_2248, class_2680> stateFactory) {
        return blockState -> this.selector;
    }
}

