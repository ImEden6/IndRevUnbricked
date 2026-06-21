/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1087
 *  net.minecraft.class_1100
 *  net.minecraft.class_2960
 *  net.minecraft.class_3665
 *  net.minecraft.class_4730
 *  net.minecraft.class_7775
 *  net.minecraft.class_793
 *  net.minecraft.class_793$class_4751
 *  net.minecraft.class_809
 */
package dev.notalpha.dashloader.client.model.fallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_1058;
import net.minecraft.class_1087;
import net.minecraft.class_1100;
import net.minecraft.class_2960;
import net.minecraft.class_3665;
import net.minecraft.class_4730;
import net.minecraft.class_7775;
import net.minecraft.class_793;
import net.minecraft.class_809;

public class UnbakedBakedModel
extends class_793
implements class_1100 {
    private final class_1087 bakedModel;

    public UnbakedBakedModel(class_1087 bakedModel, class_2960 identifier) {
        super(null, List.of(), Map.of(), Boolean.valueOf(bakedModel.method_4708()), class_793.class_4751.field_21858, class_809.field_4301, List.of());
        this.field_4252 = identifier.toString();
        this.bakedModel = bakedModel;
    }

    public Collection<class_2960> method_4755() {
        return List.of();
    }

    public void method_45785(Function<class_2960, class_1100> modelLoader) {
    }

    public class_1087 method_4753(class_7775 baker, Function<class_4730, class_1058> textureGetter, class_3665 rotationContainer, class_2960 modelId) {
        return this.bakedModel;
    }
}

