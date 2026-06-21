/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.scan.annotations.HyphenAnnotation
 */
package dev.notalpha.dashloader.io.def;

import dev.quantumfusion.hyphen.scan.annotations.HyphenAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@HyphenAnnotation
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE_USE})
public @interface DataUnsafeByteBuffer {
}

