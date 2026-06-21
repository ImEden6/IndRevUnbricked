/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package dev.notalpha.dashloader.registry;

import dev.notalpha.dashloader.DashObjectClass;
import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public final class FactoryBinding<R, D extends DashObject<R>> {
    private final MethodHandle method;
    private final FactoryFunction creator;

    public FactoryBinding(MethodHandle method, FactoryFunction creator) {
        this.method = method;
        this.creator = creator;
    }

    public static <R, D extends DashObject<R>> FactoryBinding<R, D> create(DashObjectClass<R, D> dashObject) {
        Class dashClass = dashObject.getDashClass();
        FactoryBinding<R, D> factory = FactoryBinding.tryScanCreators((look, type) -> look.findConstructor(dashClass, type.changeReturnType(Void.TYPE)), dashObject);
        if (factory == null) {
            factory = FactoryBinding.tryScanCreators((look, type) -> look.findStatic(dashClass, "factory", type), dashObject);
        }
        if (factory == null) {
            factory = FactoryBinding.tryScanCreators((look, type) -> look.findStatic(dashClass, "factory", type), dashObject);
        }
        if (factory == null) {
            throw new RuntimeException("Could not find a way to create " + dashClass.getSimpleName() + ". Create the method and/or check if it's accessible.");
        }
        return factory;
    }

    public D create(R raw, RegistryWriter writer) {
        try {
            return (D)((DashObject)this.creator.create(this.method, raw, writer));
        }
        catch (Throwable e) {
            throw new RuntimeException("Could not create DashObject " + raw.getClass().getSimpleName(), e);
        }
    }

    @Nullable
    private static <R, D extends DashObject<R>> FactoryBinding<R, D> tryScanCreators(MethodTester tester, DashObjectClass<R, D> dashObject) {
        for (InvokeType value : InvokeType.values()) {
            Class<?>[] apply = value.parameters.apply(dashObject);
            try {
                MethodHandle method = tester.getMethod(MethodHandles.publicLookup(), MethodType.methodType(dashObject.getTargetClass(), apply));
                if (method == null) continue;
                return new FactoryBinding<R, D>(method, value.creator);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    @FunctionalInterface
    private static interface FactoryFunction {
        public Object create(MethodHandle var1, Object var2, RegistryWriter var3) throws Throwable;
    }

    @FunctionalInterface
    private static interface MethodTester {
        public MethodHandle getMethod(MethodHandles.Lookup var1, MethodType var2) throws Throwable;
    }

    /*
     * Exception performing whole class analysis.
     */
    private static final class InvokeType
    extends Enum<InvokeType> {
        public static final /* enum */ InvokeType FULL;
        public static final /* enum */ InvokeType WRITER;
        public static final /* enum */ InvokeType RAW;
        public static final /* enum */ InvokeType EMPTY;
        private final FactoryFunction creator;
        private final Function<DashObjectClass<?, ?>, Class<?>[]> parameters;
        private static final /* synthetic */ InvokeType[] $VALUES;

        public static InvokeType[] values() {
            return (InvokeType[])$VALUES.clone();
        }

        public static InvokeType valueOf(String name) {
            return Enum.valueOf(InvokeType.class, name);
        }

        private InvokeType(FactoryFunction creator, Function<DashObjectClass<?, ?>, Class<?>[]> parameters) {
            super(string, n);
            this.creator = creator;
            this.parameters = parameters;
        }

        private static /* synthetic */ Class[] lambda$static$7(DashObjectClass doc) {
            return new Class[0];
        }

        private static /* synthetic */ Object lambda$static$6(MethodHandle mh, Object raw, RegistryWriter writer) throws Throwable {
            return mh.invoke();
        }

        private static /* synthetic */ Class[] lambda$static$5(DashObjectClass doc) {
            return new Class[]{doc.getTargetClass()};
        }

        private static /* synthetic */ Object lambda$static$4(MethodHandle mh, Object raw, RegistryWriter writer) throws Throwable {
            return mh.invoke(raw);
        }

        private static /* synthetic */ Class[] lambda$static$3(DashObjectClass doc) {
            return new Class[]{RegistryWriter.class};
        }

        private static /* synthetic */ Object lambda$static$2(MethodHandle mh, Object raw, RegistryWriter writer) throws Throwable {
            return mh.invoke(writer);
        }

        private static /* synthetic */ InvokeType[] $values() {
            return new InvokeType[]{FULL, WRITER, RAW, EMPTY};
        }

        /*
         * Exception decompiling
         */
        static {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * java.lang.UnsupportedOperationException
             *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.NewAnonymousArray.getDimSize(NewAnonymousArray.java:142)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.isNewArrayLambda(LambdaRewriter.java:455)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:409)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:167)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:105)
             *     at org.benf.cfr.reader.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper.applyForwards(ExpressionRewriterHelper.java:12)
             *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractConstructorInvokation.applyExpressionRewriter(AbstractConstructorInvokation.java:65)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:103)
             *     at org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredAssignment.rewriteExpressions(StructuredAssignment.java:146)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewrite(LambdaRewriter.java:88)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.rewriteLambdas(Op04StructuredStatement.java:1137)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:912)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }
    }
}

