package jna.ffi;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import static io.github.fasterjna.jna.ffi.Memory.memcpy;

public final class Dispatch {

    private Dispatch() {
        throw new AssertionError("No jna.ffi.Dispatch instances for you!");
    }

    private static long parseMemoryBytes(String memoryString) {
        long multiplier;
        char unitChar = memoryString.charAt(memoryString.length() - 1);
        if (unitChar == 'k' || unitChar == 'K') multiplier = 1024;
        else if (unitChar == 'm' || unitChar == 'M') multiplier = 1024 * 1024;
        else if (unitChar == 'g' || unitChar == 'G') multiplier = 1024 * 1024 * 1024;
        else if (unitChar == 't' || unitChar == 'T') multiplier = 1024L * 1024L * 1024L * 1024L;
        else multiplier = 1;
        if (multiplier > 1) memoryString = memoryString.substring(0, memoryString.length() - 1);
        return Math.multiplyExact(multiplier, Long.parseLong(memoryString));
    }
    private static final long BUFFER_SIZE;
    static {
        long value;
        try {
            value = parseMemoryBytes(System.getProperty("jnablow.dispatch.bufferSize"));
        }
        catch (Throwable e) {
            value = 4096;
        }
        BUFFER_SIZE = value;
    }
    private static final ThreadLocal<Memory> BUFFER = new ThreadLocal<Memory>() {
        @Override
        protected Memory initialValue() {
            return BUFFER_SIZE == 0L ? null : new Memory(BUFFER_SIZE);
        }
    };

    public static int invokeI0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getInt(0);
    }

    public static int invokeI1(CallContext context, Function function, int arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 12L);
            buffer.setInt(12L, arg1);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 8L));
            buffer.setInt(8L, arg1);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static int invokeI2(CallContext context, Function function, int arg1, int arg2) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 20L);
            buffer.setLong(12L, rvalue + 24L);
            buffer.setInt(20L, arg1);
            buffer.setInt(24L, arg2);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 12L));
            buffer.setInt(8L, (int) (rvalue + 16L));
            buffer.setInt(12L, arg1);
            buffer.setInt(16L, arg2);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static int invokeI3(CallContext context, Function function, int arg1, int arg2, int arg3) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 28L);
            buffer.setLong(12L, rvalue + 32L);
            buffer.setLong(20L, rvalue + 36L);
            buffer.setInt(28L, arg1);
            buffer.setInt(32L, arg2);
            buffer.setInt(36L, arg3);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 20L));
            buffer.setInt(8L, (int) (rvalue + 28L));
            buffer.setInt(12L, (int) (rvalue + 36L));
            buffer.setInt(16L, arg1);
            buffer.setInt(20L, arg2);
            buffer.setInt(24L, arg3);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static int invokeI4(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 36L);
            buffer.setLong(12L, rvalue + 40L);
            buffer.setLong(20L, rvalue + 44L);
            buffer.setLong(28L, rvalue + 48L);
            buffer.setInt(36L, arg1);
            buffer.setInt(40L, arg2);
            buffer.setInt(44L, arg3);
            buffer.setInt(48L, arg4);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 20L));
            buffer.setInt(8L, (int) (rvalue + 24L));
            buffer.setInt(12L, (int) (rvalue + 28L));
            buffer.setInt(16L, (int) (rvalue + 32L));
            buffer.setInt(20L, arg1);
            buffer.setInt(24L, arg2);
            buffer.setInt(28L, arg3);
            buffer.setInt(32L, arg4);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static int invokeI5(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4, int arg5) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 44L);
            buffer.setLong(12L, rvalue + 48L);
            buffer.setLong(20L, rvalue + 52L);
            buffer.setLong(28L, rvalue + 56L);
            buffer.setLong(36L, rvalue + 60L);
            buffer.setInt(44L, arg1);
            buffer.setInt(48L, arg2);
            buffer.setInt(52L, arg3);
            buffer.setInt(56L, arg4);
            buffer.setInt(60L, arg5);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 24L));
            buffer.setInt(8L, (int) (rvalue + 28L));
            buffer.setInt(12L, (int) (rvalue + 32L));
            buffer.setInt(16L, (int) (rvalue + 36L));
            buffer.setInt(20L, (int) (rvalue + 40L));
            buffer.setInt(24L, arg1);
            buffer.setInt(28L, arg2);
            buffer.setInt(32L, arg3);
            buffer.setInt(36L, arg4);
            buffer.setInt(40L, arg5);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static int invokeI6(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setInt(0, 0);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(4L, rvalue + 52L);
            buffer.setLong(12L, rvalue + 56L);
            buffer.setLong(20L, rvalue + 60L);
            buffer.setLong(28L, rvalue + 64L);
            buffer.setLong(36L, rvalue + 68L);
            buffer.setLong(44L, rvalue + 72L);
            buffer.setInt(52L, arg1);
            buffer.setInt(56L, arg2);
            buffer.setInt(60L, arg3);
            buffer.setInt(64L, arg4);
            buffer.setInt(68L, arg5);
            buffer.setInt(72L, arg6);
        }
        else {
            buffer.setInt(4L, (int) (rvalue + 28L));
            buffer.setInt(8L, (int) (rvalue + 32L));
            buffer.setInt(12L, (int) (rvalue + 36L));
            buffer.setInt(16L, (int) (rvalue + 40L));
            buffer.setInt(20L, (int) (rvalue + 44L));
            buffer.setInt(24L, (int) (rvalue + 48L));
            buffer.setInt(28L, arg1);
            buffer.setInt(32L, arg2);
            buffer.setInt(36L, arg3);
            buffer.setInt(40L, arg4);
            buffer.setInt(44L, arg5);
            buffer.setInt(48L, arg6);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4L);
        return buffer.getInt(0);
    }

    public static long invokeL0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getLong(0);
    }

    public static long invokeL1(CallContext context, Function function, long arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 16L);
            buffer.setLong(16L, arg1);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 12L));
            buffer.setLong(12L, arg1);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static long invokeL2(CallContext context, Function function, long arg1, long arg2) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 24L);
            buffer.setLong(16L, rvalue + 32L);
            buffer.setLong(24L, arg1);
            buffer.setLong(32L, arg2);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 16L));
            buffer.setInt(12L, (int) (rvalue + 24L));
            buffer.setLong(16L, arg1);
            buffer.setLong(24L, arg2);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static long invokeL3(CallContext context, Function function, long arg1, long arg2, long arg3) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 32L);
            buffer.setLong(16L, rvalue + 40L);
            buffer.setLong(24L, rvalue + 48L);
            buffer.setLong(32L, arg1);
            buffer.setLong(40L, arg2);
            buffer.setLong(48L, arg3);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 20L));
            buffer.setInt(12L, (int) (rvalue + 28L));
            buffer.setInt(16L, (int) (rvalue + 36L));
            buffer.setLong(20L, arg1);
            buffer.setLong(28L, arg2);
            buffer.setLong(36L, arg3);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static long invokeL4(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 40L);
            buffer.setLong(16L, rvalue + 48L);
            buffer.setLong(24L, rvalue + 56L);
            buffer.setLong(32L, rvalue + 64L);
            buffer.setLong(40L, arg1);
            buffer.setLong(48L, arg2);
            buffer.setLong(56L, arg3);
            buffer.setLong(64L, arg4);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 24L));
            buffer.setInt(12L, (int) (rvalue + 32L));
            buffer.setInt(16L, (int) (rvalue + 40L));
            buffer.setInt(20L, (int) (rvalue + 48L));
            buffer.setLong(24L, arg1);
            buffer.setLong(32L, arg2);
            buffer.setLong(40L, arg3);
            buffer.setLong(48L, arg4);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static long invokeL5(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4, long arg5) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 48L);
            buffer.setLong(16L, rvalue + 56L);
            buffer.setLong(24L, rvalue + 64L);
            buffer.setLong(32L, rvalue + 72L);
            buffer.setLong(40L, rvalue + 80L);
            buffer.setLong(48L, arg1);
            buffer.setLong(56L, arg2);
            buffer.setLong(64L, arg3);
            buffer.setLong(72L, arg4);
            buffer.setLong(80L, arg5);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 28L));
            buffer.setInt(12L, (int) (rvalue + 36L));
            buffer.setInt(16L, (int) (rvalue + 44L));
            buffer.setInt(20L, (int) (rvalue + 52L));
            buffer.setInt(24L, (int) (rvalue + 60L));
            buffer.setLong(28L, arg1);
            buffer.setLong(36L, arg2);
            buffer.setLong(44L, arg3);
            buffer.setLong(52L, arg4);
            buffer.setLong(60L, arg5);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static long invokeL6(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        buffer.setLong(0, 0L);
        if (Native.POINTER_SIZE == 8L) {
            buffer.setLong(8L, rvalue + 56L);
            buffer.setLong(16L, rvalue + 64L);
            buffer.setLong(24L, rvalue + 72L);
            buffer.setLong(32L, rvalue + 80L);
            buffer.setLong(40L, rvalue + 88L);
            buffer.setLong(48L, rvalue + 96L);
            buffer.setLong(56L, arg1);
            buffer.setLong(64L, arg2);
            buffer.setLong(72L, arg3);
            buffer.setLong(80L, arg4);
            buffer.setLong(88L, arg5);
            buffer.setLong(96L, arg6);
        }
        else {
            buffer.setInt(8L, (int) (rvalue + 32L));
            buffer.setInt(12L, (int) (rvalue + 40L));
            buffer.setInt(16L, (int) (rvalue + 48L));
            buffer.setInt(20L, (int) (rvalue + 56L));
            buffer.setInt(24L, (int) (rvalue + 64L));
            buffer.setInt(28L, (int) (rvalue + 72L));
            buffer.setLong(32L, arg1);
            buffer.setLong(40L, arg2);
            buffer.setLong(48L, arg3);
            buffer.setLong(56L, arg4);
            buffer.setLong(64L, arg5);
            buffer.setLong(72L, arg6);
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8L);
        return buffer.getLong(0);
    }

    public static Object call(CallContext context, Function function, Object... args) {
        Memory buffer = BUFFER.get();

        Type rtype = context.rtype;
        Type[] atypes = context.atypes;
        if (Native.POINTER_SIZE == 8L) buffer.setLong(0, 0L);
        else buffer.setInt(0, 0);
        long rvalue = Pointer.nativeValue(buffer);
        long avaluesOffset;
        if (rtype.compound) avaluesOffset = Native.POINTER_SIZE;
        else avaluesOffset = rtype.size;
        if (atypes != null) {
            long avalueOffset = avaluesOffset + (long) atypes.length * Native.POINTER_SIZE;
            for (int i = 0; i < atypes.length; i ++) {
                Object arg = args[i + (rtype.compound ? 1 : 0)];
                Type atype = atypes[i];
                if (atype.compound) buffer.setPointer(avaluesOffset + (long) Native.POINTER_SIZE * i, (Pointer) arg);
                else {
                    if (Native.POINTER_SIZE == 8L) buffer.setLong(avaluesOffset + (long) Native.POINTER_SIZE * i, rvalue + avalueOffset);
                    else buffer.setInt(avaluesOffset + (long) Native.POINTER_SIZE * i, (int) (rvalue + avalueOffset));
                    if (atype == Type.SINT8) buffer.setByte(avalueOffset, ((Number) arg).byteValue());
                    else if (atype == Type.SINT16
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 2L))
                        buffer.setShort(avalueOffset, ((Number) arg).shortValue());
                    else if (atype == Type.JCHAR) buffer.setChar(avalueOffset, (Character) arg);
                    else if (atype == Type.SINT32
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                            || (atype == Type.LONG && Native.LONG_SIZE == 4L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
                        buffer.setInt(avalueOffset, ((Number) arg).intValue());
                    else if (atype == Type.SINT64
                            || (atype == Type.LONG && Native.LONG_SIZE == 8L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
                        buffer.setLong(avalueOffset, ((Number) arg).longValue());
                    else if (atype == Type.FLOAT) buffer.setFloat(avalueOffset, ((Number) arg).floatValue());
                    else if (atype == Type.DOUBLE) buffer.setDouble(avalueOffset, ((Number) arg).doubleValue());
                    else if (atype == Type.POINTER) buffer.setPointer(avalueOffset, (Pointer) arg);
                    else if (atype == Type.BOOLEAN) {
                        if (Native.POINTER_SIZE == 8L) buffer.setLong(avalueOffset, ((Boolean) arg) ? 1L : 0L);
                        else buffer.setInt(avalueOffset, ((Boolean) arg) ? 1 : 0);
                    }
                    avalueOffset += atype.size;
                }
            }
        }
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + avaluesOffset);

        if (rtype == Type.VOID) return null;
        else if (rtype == Type.SINT8) return buffer.getByte(0);
        else if (rtype == Type.SINT16
                || (rtype == Type.WCHAR && Native.WCHAR_SIZE == 2L))
            return buffer.getShort(0);
        else if (rtype == Type.JCHAR) return buffer.getChar(0);
        else if (rtype == Type.SINT32
                || (rtype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                || (rtype == Type.LONG && Native.LONG_SIZE == 4L)
                || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
            return buffer.getInt(0);
        else if (rtype == Type.SINT64
                || (rtype == Type.LONG && Native.LONG_SIZE == 8L)
                || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
            return buffer.getLong(0);
        else if (rtype == Type.FLOAT) return buffer.getFloat(0);
        else if (rtype == Type.DOUBLE) return buffer.getDouble(0);
        else if (rtype == Type.POINTER) return buffer.getPointer(0);
        else if (rtype == Type.BOOLEAN) {
            if (Native.POINTER_SIZE == 8L) return buffer.getLong(0) != 0L;
            else return buffer.getInt(0) != 0;
        }
        else {
            Pointer arg = (Pointer) args[0];
            memcpy(arg, buffer.getPointer(0), rtype.size);
            return arg;
        }
    }

}
