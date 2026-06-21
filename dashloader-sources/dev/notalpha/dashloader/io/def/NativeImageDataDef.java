/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.quantumfusion.hyphen.SerializerHandler
 *  dev.quantumfusion.hyphen.codegen.MethodHandler
 *  dev.quantumfusion.hyphen.codegen.Variable
 *  dev.quantumfusion.hyphen.codegen.def.BufferDef
 *  dev.quantumfusion.hyphen.codegen.def.MethodDef
 *  dev.quantumfusion.hyphen.codegen.statement.IfElse
 *  dev.quantumfusion.hyphen.scan.type.Clazz
 *  org.lwjgl.system.MemoryUtil
 */
package dev.notalpha.dashloader.io.def;

import dev.notalpha.dashloader.io.def.NativeImageData;
import dev.quantumfusion.hyphen.SerializerHandler;
import dev.quantumfusion.hyphen.codegen.MethodHandler;
import dev.quantumfusion.hyphen.codegen.Variable;
import dev.quantumfusion.hyphen.codegen.def.BufferDef;
import dev.quantumfusion.hyphen.codegen.def.MethodDef;
import dev.quantumfusion.hyphen.codegen.statement.IfElse;
import dev.quantumfusion.hyphen.scan.type.Clazz;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;

public class NativeImageDataDef
extends MethodDef {
    private ByteBufferDef bytebufferDef;

    public NativeImageDataDef(SerializerHandler<?, ?> handler, Clazz clazz) {
        super(handler, clazz);
    }

    public void scan(SerializerHandler<?, ?> handler, Clazz clazz) {
        this.bytebufferDef = new ByteBufferDef(new Clazz(handler, ByteBuffer.class), handler);
    }

    protected void writeMethodPut(MethodHandler mh, Runnable valueLoad) {
        mh.loadIO();
        valueLoad.run();
        mh.visitFieldInsn(180, NativeImageData.class, "stb", Boolean.TYPE);
        mh.putIO(Boolean.TYPE);
        this.bytebufferDef.writePut(mh, () -> {
            valueLoad.run();
            mh.visitFieldInsn(180, NativeImageData.class, "buffer", ByteBuffer.class);
        });
    }

    protected void writeMethodGet(MethodHandler mh) {
        mh.typeOp(187, NativeImageData.class);
        mh.op(new int[]{89});
        mh.loadIO();
        mh.getIO(Boolean.TYPE);
        mh.op(new int[]{89});
        Variable stb = mh.addVar("stb", Boolean.TYPE);
        mh.varOp(54, stb);
        this.bytebufferDef.stbVariable = stb;
        this.bytebufferDef.writeGet(mh);
        mh.op(new int[]{95});
        mh.callInst(183, NativeImageData.class, "<init>", Void.TYPE, new Class[]{ByteBuffer.class, Boolean.TYPE});
    }

    protected void writeMethodMeasure(MethodHandler mh, Runnable valueLoad) {
        this.bytebufferDef.writeMeasure(mh, () -> {
            valueLoad.run();
            mh.visitFieldInsn(180, NativeImageData.class, "buffer", ByteBuffer.class);
        });
    }

    public long getStaticSize() {
        return this.bytebufferDef.getStaticSize() + 1L;
    }

    private static class ByteBufferDef
    extends BufferDef {
        private Variable stbVariable;

        public ByteBufferDef(Clazz clazz, SerializerHandler<?, ?> serializerHandler) {
            super(clazz, serializerHandler);
        }

        protected void allocateBuffer(MethodHandler mh) {
            mh.varOp(21, this.stbVariable);
            try (IfElse thing = new IfElse(mh, 153);){
                mh.op(new int[]{4, 95});
                mh.callInst(184, MemoryUtil.class, "memCalloc", ByteBuffer.class, new Class[]{Integer.TYPE, Integer.TYPE});
                thing.elseEnd();
                mh.callInst(184, MemoryUtil.class, "memAlloc", ByteBuffer.class, new Class[]{Integer.TYPE});
            }
        }
    }
}

