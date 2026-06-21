/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1095
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2960
 *  net.minecraft.class_815
 *  org.apache.commons.lang3.tuple.Pair
 */
package dev.notalpha.dashloader.client.model;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.cache.CacheStatus;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import dev.notalpha.dashloader.client.model.ModelModule;
import dev.notalpha.dashloader.mixin.accessor.MultipartBakedModelAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.class_1087;
import net.minecraft.class_1095;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2960;
import net.minecraft.class_815;
import org.apache.commons.lang3.tuple.Pair;

public class DashMultipartBakedModel
implements DashObject<class_1095> {
    public final List<Component> components;

    public DashMultipartBakedModel(List<Component> components) {
        this.components = components;
    }

    public DashMultipartBakedModel(class_1095 model, RegistryWriter writer) {
        MultipartBakedModelAccessor access = (MultipartBakedModelAccessor)model;
        List<Pair<Predicate<class_2680>, class_1087>> accessComponents = access.getComponents();
        int size = accessComponents.size();
        this.components = new ArrayList<Component>();
        Pair<List<class_815>, class_2689<class_2248, class_2680>> selectors = ModelModule.MULTIPART_PREDICATES.get(CacheStatus.SAVE).get(model);
        for (int i = 0; i < size; ++i) {
            class_1087 componentModel = (class_1087)accessComponents.get(i).getRight();
            class_815 selector = (class_815)((List)selectors.getKey()).get(i);
            class_2960 componentIdentifier = ModelModule.getStateManagerIdentifier((class_2689<class_2248, class_2680>)((class_2689)selectors.getRight()));
            this.components.add(new Component(writer.add(componentModel), writer.add(selector), writer.add(componentIdentifier)));
        }
    }

    @Override
    public class_1095 export(RegistryReader reader) {
        ArrayList componentsOut = new ArrayList(this.components.size());
        this.components.forEach(component -> {
            class_1087 compModel = (class_1087)reader.get(component.model);
            class_2960 compIdentifier = (class_2960)reader.get(component.identifier);
            class_815 compSelector = (class_815)reader.get(component.selector);
            Predicate predicate = compSelector.getPredicate(ModelModule.getStateManager(compIdentifier));
            componentsOut.add(Pair.of((Object)predicate, (Object)compModel));
        });
        class_1095 multipartBakedModel = new class_1095(componentsOut);
        MultipartBakedModelAccessor access = (MultipartBakedModelAccessor)multipartBakedModel;
        access.setStateCache(Collections.synchronizedMap(access.getStateCache()));
        return multipartBakedModel;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DashMultipartBakedModel that = (DashMultipartBakedModel)o;
        return this.components.equals(that.components);
    }

    public int hashCode() {
        return this.components.hashCode();
    }

    public static final class Component {
        public final int model;
        public final int selector;
        public final int identifier;

        public Component(int model, int selector, int identifier) {
            this.model = model;
            this.selector = selector;
            this.identifier = identifier;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Component component = (Component)o;
            if (this.model != component.model) {
                return false;
            }
            if (this.selector != component.selector) {
                return false;
            }
            return this.identifier == component.identifier;
        }

        public int hashCode() {
            int result = this.model;
            result = 31 * result + this.selector;
            result = 31 * result + this.identifier;
            return result;
        }
    }
}

