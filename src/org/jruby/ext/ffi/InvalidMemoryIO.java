

package org.jruby.ext.ffi;

import org.jruby.Ruby;

/**
 * An implementation of MemoryIO that throws an exception on any access.
 */
public abstract class InvalidMemoryIO implements MemoryIO {
    private final Ruby runtime;
    private final String message;
    
    public InvalidMemoryIO(Ruby runtime) {
        this(runtime, "Invalid memory access");

    }
    public InvalidMemoryIO(Ruby runtime, String message) {
        this.runtime = runtime;
        this.message = message;
    }
    
    private final RuntimeException ex() {
        return runtime.newRuntimeError(message);
    }

    public final byte getByte(long offset) {
        throw ex();
    }

    public final short getShort(long offset) {
        throw ex();
    }

    public final int getInt(long offset) {
        throw ex();
    }

    public final long getLong(long offset) {
        throw ex();
    }

    public final long getNativeLong(long offset) {
        throw ex();
    }

    public final float getFloat(long offset) {
        throw ex();
    }

    public final double getDouble(long offset) {
        throw ex();
    }

    public final MemoryIO getMemoryIO(long offset) {
        throw ex();
    }

    public final long getAddress(long offset) {
        throw ex();
    }

    public final void putByte(long offset, byte value) {
        throw ex();
    }

    public final void putShort(long offset, short value) {
        throw ex();
    }

    public final void putInt(long offset, int value) {
        throw ex();
    }

    public final void putLong(long offset, long value) {
        throw ex();
    }

    public final void putNativeLong(long offset, long value) {
        throw ex();
    }

    public final void putFloat(long offset, float value) {
        throw ex();
    }

    public final void putDouble(long offset, double value) {
        throw ex();
    }

    public final void putMemoryIO(long offset, MemoryIO value) {
        throw ex();
    }
    public final void putAddress(long offset, long value) {
        throw ex();
    }
    public final void get(long offset, byte[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, byte[] src, int off, int len) {
        throw ex();
    }

    public final void get(long offset, short[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, short[] src, int off, int len) {
        throw ex();
    }

    public final void get(long offset, int[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, int[] src, int off, int len) {
        throw ex();
    }

    public final void get(long offset, long[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, long[] src, int off, int len) {
        throw ex();
    }

    public final void get(long offset, float[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, float[] src, int off, int len) {
        throw ex();
    }

    public final void get(long offset, double[] dst, int off, int len) {
        throw ex();
    }

    public final void put(long offset, double[] src, int off, int len) {
        throw ex();
    }

    public final int indexOf(long offset, byte value) {
        throw ex();
    }

    public final int indexOf(long offset, byte value, int maxlen) {
        throw ex();
    }

    public final void setMemory(long offset, long size, byte value) {
        throw ex();
    }

    public final void clear() {
        throw ex();
    }
}
