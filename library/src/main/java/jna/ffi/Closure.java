package jna.ffi;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.internal.Cleaner;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;

import static io.github.fasterjna.jna.ffi.Memory.*;

public final class Closure {

    private final CallContext context;
    private final long address;

    public static Closure newClosure(CallContext context, ClosureHandler handler) {
        return new Closure(context, handler);
    }

    private Closure(CallContext context, ClosureHandler handler) {
        this.context = Objects.requireNonNull(context);
        Objects.requireNonNull(handler);
        Type rtype = context.rtype;
        Memory rvalue_memory = rtype.compound ? new Memory(rtype.size) : null;
        Type[] atypes = context.atypes;
        Object[] args = new Object[atypes.length];
        Native.ffi_callback callback = (cif, resp, argp) -> {
            handler.preInvoke(Closure.this);
            try {
                Pointer rvalue = new Pointer(resp);
                Pointer avalues = new Pointer(argp).getPointer(0);
                long avalueOffset = 0;
                for (int i = 0; i < atypes.length; i ++) {
                    Type atype = atypes[i];
                    if (atype == Type.SINT8) args[i] = avalues.getByte(avalueOffset);
                    else if (atype == Type.SINT16
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 2L))
                        args[i] = avalues.getShort(avalueOffset);
                    else if (atype == Type.JCHAR) args[i] = avalues.getChar(avalueOffset);
                    else if (atype == Type.SINT32
                            || (atype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                            || (atype == Type.LONG && Native.LONG_SIZE == 4L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
                        args[i] = avalues.getInt(avalueOffset);
                    else if (atype == Type.SINT64
                            || (atype == Type.LONG && Native.LONG_SIZE == 8L)
                            || (atype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
                        args[i] = avalues.getLong(avalueOffset);
                    else if (atype == Type.FLOAT) args[i] = avalues.getFloat(avalueOffset);
                    else if (atype == Type.DOUBLE) args[i] = avalues.getDouble(avalueOffset);
                    else if (atype == Type.POINTER) args[i] = avalues.getPointer(avalueOffset);
                    else if (atype == Type.BOOLEAN) {
                        if (Native.POINTER_SIZE == 8L) args[i] = avalues.getLong(avalueOffset) != 0L;
                        else args[i] = avalues.getInt(avalueOffset) != 0;
                    }
                    else args[i] = avalues.share(avalueOffset, atype.size);
                    avalueOffset += Native.POINTER_SIZE;
                }
                Object result = handler.invoke(args);
                if (rtype != Type.VOID) {
                    Objects.requireNonNull(result);
                    if (rtype == Type.SINT8) rvalue.setByte(0, ((Number) result).byteValue());
                    else if (rtype == Type.SINT16
                            || (rtype == Type.WCHAR && Native.WCHAR_SIZE == 2L))
                        rvalue.setShort(0, ((Number) result).shortValue());
                    else if (rtype == Type.JCHAR) rvalue.setChar(0, (Character) result);
                    else if (rtype == Type.SINT32
                            || (rtype == Type.WCHAR && Native.WCHAR_SIZE == 4L)
                            || (rtype == Type.LONG && Native.LONG_SIZE == 4L)
                            || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 4L))
                        rvalue.setInt(0, ((Number) result).intValue());
                    else if (rtype == Type.SINT64
                            || (rtype == Type.LONG && Native.LONG_SIZE == 8L)
                            || (rtype == Type.SIZE && Native.SIZE_T_SIZE == 8L))
                        rvalue.setLong(0, ((Number) result).longValue());
                    else if (rtype == Type.FLOAT) rvalue.setFloat(0, ((Number) result).floatValue());
                    else if (rtype == Type.DOUBLE) rvalue.setDouble(0, ((Number) result).doubleValue());
                    else if (rtype == Type.POINTER) rvalue.setPointer(0, (Pointer) result);
                    else if (rtype == Type.BOOLEAN) {
                        if (Native.POINTER_SIZE == 8L) rvalue.setLong(0, ((Boolean) result) ? 1L : 0L);
                        else rvalue.setInt(0, ((Boolean) result) ? 1 : 0);
                    }
                    else {
                        memcpy(rvalue_memory, (Pointer) result, rtype.size);
                        rvalue.setPointer(0, rvalue_memory);
                    }
                }
            }
            catch (RuntimeException | Error e) {
                throw e;
            }
            catch (Throwable e) {
                throw new UndeclaredThrowableException(e);
            }
            finally {
                handler.postInvoke(Closure.this);
            }
        };
        long closure = Native.ffi_prep_closure(context.handle, callback);
        if (closure == 0L) throw new IllegalStateException("Failed to initialize ffi_closure");
        address = Pointer.nativeValue(new Pointer(closure).getPointer(0));
        Cleaner.getCleaner().register(this, () -> Native.ffi_free_closure(closure));
    }

    public CallContext getCallContext() {
        return context;
    }

    public long address() {
        return address;
    }

}
