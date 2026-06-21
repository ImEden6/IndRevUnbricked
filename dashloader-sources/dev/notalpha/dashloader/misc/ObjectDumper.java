/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle
 *  org.apache.commons.lang3.builder.ReflectionToStringBuilder
 *  org.apache.commons.lang3.builder.ToStringStyle
 */
package dev.notalpha.dashloader.misc;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_1011;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ObjectDumper {
    public static String dump(Object object) {
        return ReflectionToStringBuilder.toString((Object)object, (ToStringStyle)new Style());
    }

    private static final class Style
    extends MultilineRecursiveToStringStyle {
        public Style() {
            this.setFieldNameValueSeparator(": ");
            this.setUseIdentityHashCode(false);
            this.setUseShortClassName(true);
        }

        public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value != null) {
                if (Objects.equals(fieldName, "glRef")) {
                    buffer.append("<id>");
                    return;
                }
                if (value instanceof class_1011) {
                    class_1011 image = (class_1011)value;
                    buffer.append("Image{ format: ").append(image.method_4318()).append(", size: ").append(image.method_4307()).append("x").append(image.method_4323()).append(" }");
                    return;
                }
                if (value instanceof IntBuffer) {
                    IntBuffer buffer1 = (IntBuffer)value;
                    buffer.append("IntBuffer [");
                    int limit = buffer1.limit();
                    if (limit < 50) {
                        buffer1.rewind();
                        for (int i = 0; i < limit; ++i) {
                            float v = buffer1.get();
                            buffer.append(v);
                            buffer.append(", ");
                        }
                    }
                    buffer.append("]");
                    return;
                }
                if (value instanceof FloatBuffer) {
                    FloatBuffer buffer1 = (FloatBuffer)value;
                    buffer.append("FloatBuffer [");
                    int limit = buffer1.limit();
                    if (limit < 50) {
                        buffer1.rewind();
                        for (int i = 0; i < limit; ++i) {
                            float v = buffer1.get();
                            buffer.append(v);
                            buffer.append(", ");
                        }
                    }
                    buffer.append("]");
                    return;
                }
                if (value instanceof Enum) {
                    Enum enumValue = (Enum)value;
                    buffer.append(enumValue.name());
                    return;
                }
            } else {
                buffer.append("null");
                return;
            }
            try {
                StringBuffer builder = new StringBuffer();
                super.appendDetail(builder, fieldName, value);
                String s = builder.toString();
                String result = s.split("@")[0];
                buffer.append(result);
            }
            catch (Exception e) {
                e.printStackTrace();
                buffer.append("unknown");
                try {
                    Field spaces = MultilineRecursiveToStringStyle.class.getDeclaredField("spaces");
                    spaces.setAccessible(true);
                    spaces.setInt((Object)this, spaces.getInt((Object)this) - 2);
                }
                catch (IllegalAccessException | NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
            buffer.append(this.getArrayStart());
            ArrayList entries = new ArrayList(map.entrySet());
            entries.sort((o1, o2) -> o1.getKey().toString().compareTo(o2.toString()));
            entries.forEach(entry -> {
                buffer.append(this.getArraySeparator());
                this.appendDetail(buffer, String.valueOf(entry.getKey()), entry.getValue());
            });
            buffer.append(this.getArrayEnd());
        }

        protected void appendIdentityHashCode(StringBuffer buffer, Object object) {
        }
    }
}

