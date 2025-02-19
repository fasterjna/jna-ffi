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
            value = parseMemoryBytes(System.getProperty("jna.ffi.dispatch.bufferSize"));
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

    public static void invoke(CallContext context, Function function) {
        Native.ffi_call(context.handle, Pointer.nativeValue(function), 0L, 0L);
    }

    public static int invokeI0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getInt(0);
    }

    public static float invokeF0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getFloat(0);
    }

    public static long invokeL0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getLong(0);
    }

    public static double invokeD0(CallContext context, Function function) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, 0L);
        return buffer.getDouble(0);
    }

    private interface Int64Adapter {
        void setAddress(Pointer pointer, long offset, long value);
        long getAddress(Pointer pointer, long offset);
        Int64Adapter SIZE32 = new Int64Adapter() {
            @Override
            public void setAddress(Pointer pointer, long offset, long value) {
                pointer.setInt(offset, (int) value);
            }
            @Override
            public long getAddress(Pointer pointer, long offset) {
                return pointer.getInt(offset) & 0xFFFFFFFFL;
            }
        };
        Int64Adapter SIZE64 = new Int64Adapter() {
            @Override
            public void setAddress(Pointer pointer, long offset, long value) {
                pointer.setLong(offset, value);
            }
            @Override
            public long getAddress(Pointer pointer, long offset) {
                return pointer.getLong(offset);
            }
        };
        Int64Adapter ADDRESS = Native.POINTER_SIZE == 8L ? SIZE64 : SIZE32;
    }

    public static int invokeI1(CallContext context, Function function, int arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static float invokeF1(CallContext context, Function function, float arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setFloat(offset, arg1);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getFloat(0);
    }

    public static int invokeI2(CallContext context, Function function, int arg1, int arg2) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE * 2L;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setInt(offset, arg2);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static int invokeI3(CallContext context, Function function, int arg1, int arg2, int arg3) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE * 3L;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setInt(offset, arg2);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setInt(offset, arg3);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static int invokeI4(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE * 4L;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setInt(offset, arg2);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setInt(offset, arg3);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setInt(offset, arg4);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static int invokeI5(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4, int arg5) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE * 5L;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setInt(offset, arg2);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setInt(offset, arg3);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setInt(offset, arg4);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 4L, rvalue + offset);
        buffer.setInt(offset, arg5);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static int invokeI6(CallContext context, Function function, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 4 + Native.POINTER_SIZE * 6L;
        Int64Adapter.ADDRESS.setAddress(buffer, 4, rvalue + offset);
        buffer.setInt(offset, arg1);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setInt(offset, arg2);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setInt(offset, arg3);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setInt(offset, arg4);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 4L, rvalue + offset);
        buffer.setInt(offset, arg5);
        offset += 4;
        Int64Adapter.ADDRESS.setAddress(buffer, 4 + Native.POINTER_SIZE * 5L, rvalue + offset);
        buffer.setInt(offset, arg6);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 4);
        return buffer.getInt(0);
    }

    public static long invokeL1(CallContext context, Function function, long arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static double invokeD1(CallContext context, Function function, double arg1) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setDouble(offset, arg1);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getDouble(0);
    }

    public static long invokeL2(CallContext context, Function function, long arg1, long arg2) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE * 2L;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setLong(offset, arg2);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static long invokeL3(CallContext context, Function function, long arg1, long arg2, long arg3) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE * 3L;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setLong(offset, arg2);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setLong(offset, arg3);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static long invokeL4(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE * 4L;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setLong(offset, arg2);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setLong(offset, arg3);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setLong(offset, arg4);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static long invokeL5(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4, long arg5) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE * 5L;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setLong(offset, arg2);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setLong(offset, arg3);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setLong(offset, arg4);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 4L, rvalue + offset);
        buffer.setLong(offset, arg5);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static long invokeL6(CallContext context, Function function, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6) {
        Memory buffer = BUFFER.get();
        long rvalue = Pointer.nativeValue(buffer);
        long offset = 8 + Native.POINTER_SIZE * 6L;
        Int64Adapter.ADDRESS.setAddress(buffer, 8, rvalue + offset);
        buffer.setLong(offset, arg1);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE, rvalue + offset);
        buffer.setLong(offset, arg2);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 2L, rvalue + offset);
        buffer.setLong(offset, arg3);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 3L, rvalue + offset);
        buffer.setLong(offset, arg4);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 4L, rvalue + offset);
        buffer.setLong(offset, arg5);
        offset += 8;
        Int64Adapter.ADDRESS.setAddress(buffer, 8 + Native.POINTER_SIZE * 5L, rvalue + offset);
        buffer.setLong(offset, arg6);
        Native.ffi_call(context.handle, Pointer.nativeValue(function), rvalue, rvalue + 8);
        return buffer.getLong(0);
    }

    public static Object call(CallContext context, Function function, Object... args) {
        Memory buffer = BUFFER.get();

        Type rtype = context.rtype;
        Type[] atypes = context.atypes;
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
                    Int64Adapter.ADDRESS.setAddress(buffer, avaluesOffset + (long) Native.POINTER_SIZE * i, rvalue + avalueOffset);
                    if (atype == Type.SINT8) buffer.setByte(avalueOffset, ((Number) arg).byteValue());
                    else if (atype == Type.SINT16
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 2L))
                        buffer.setShort(avalueOffset, ((Number) arg).shortValue());
                    else if (atype == Type.UNICHAR) buffer.setChar(avalueOffset, (Character) arg);
                    else if (atype == Type.SINT32
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                            || (atype == Type.SLONG && Native.LONG_SIZE == 4L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
                        buffer.setInt(avalueOffset, ((Number) arg).intValue());
                    else if (atype == Type.SINT64
                            || (atype == Type.SLONG && Native.LONG_SIZE == 8L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
                        buffer.setLong(avalueOffset, ((Number) arg).longValue());
                    else if (atype == Type.FLOAT) buffer.setFloat(avalueOffset, ((Number) arg).floatValue());
                    else if (atype == Type.DOUBLE) buffer.setDouble(avalueOffset, ((Number) arg).doubleValue());
                    else if (atype == Type.POINTER) buffer.setPointer(avalueOffset, (Pointer) arg);
                    else if (atype == Type.BOOLEAN) Int64Adapter.ADDRESS.setAddress(buffer, avalueOffset, ((Boolean) arg) ? 1L : 0L);
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
        else if (rtype == Type.UNICHAR) return buffer.getChar(0);
        else if (rtype == Type.SINT32
                || (rtype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                || (rtype == Type.SLONG && Native.LONG_SIZE == 4L)
                || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
            return buffer.getInt(0);
        else if (rtype == Type.SINT64
                || (rtype == Type.SLONG && Native.LONG_SIZE == 8L)
                || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
            return buffer.getLong(0);
        else if (rtype == Type.FLOAT) return buffer.getFloat(0);
        else if (rtype == Type.DOUBLE) return buffer.getDouble(0);
        else if (rtype == Type.POINTER) return new Pointer(Int64Adapter.ADDRESS.getAddress(buffer, 0));
        else if (rtype == Type.BOOLEAN) return Int64Adapter.ADDRESS.getAddress(buffer, 0) != 0;
        else {
            Pointer arg = (Pointer) args[0];
            memcpy(arg, buffer.getPointer(0), rtype.size);
            return arg;
        }
    }

}
