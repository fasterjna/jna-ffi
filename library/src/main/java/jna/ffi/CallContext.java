package jna.ffi;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.internal.Cleaner;
import io.github.fasterjna.jna.ffi.JNIDispatch;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

public final class CallContext {

    private static final Map<Integer, CallContext> CIF_MAP = new WeakHashMap<>();

    public static CallContext getCallContext(CallingConvention conversion, Type rtype, Type[] atypes, int atypesOffset, int nfixedargs, int ntotalargs) {
        if (ntotalargs < 0) throw new IllegalArgumentException("Negative array length");
        //else if (ntotalargs > 255) throw new IllegalArgumentException("parameter limit exceeded: " + ntotalargs);
        if (nfixedargs < 0) nfixedargs = ntotalargs;
        else if (nfixedargs > ntotalargs)
            throw new IllegalArgumentException(nfixedargs + " > " + ntotalargs);
        if (atypes == null) {
            if (ntotalargs != 0) throw new IllegalArgumentException(ntotalargs + " > 0");
        }
        else {
            Type[] _atypes = new Type[ntotalargs];
            for (int i = 0; i < ntotalargs; i ++) {
                Type atype = atypes[atypesOffset + i];
                if (atype == Type.VOID) throw new IllegalArgumentException("Unsupported type");
                _atypes[i] = atype;
            }
            atypes = _atypes;
        }
        int hashCode = Long.hashCode(rtype.handle);
        hashCode = 31 * hashCode + nfixedargs;
        hashCode = 31 * hashCode + ntotalargs;
        hashCode = 31 * hashCode + Arrays.hashCode(atypes);
        Integer boxed = hashCode;
        if (!CIF_MAP.containsKey(boxed)) {
            synchronized (CIF_MAP) {
                if (!CIF_MAP.containsKey(boxed)) {
                    Memory atypes_memory = ntotalargs == 0 ? null : new Memory((long) ntotalargs * Native.POINTER_SIZE);
                    for (int i = 0; i < ntotalargs; i ++) {
                        if (Native.POINTER_SIZE == 8L) atypes_memory.setLong(i * 8L, atypes[i].handle);
                        else atypes_memory.setInt(i * 4L, (int) atypes[i].handle);
                    }
                    long handle = Native.ffi_prep_cif(conversion == null ? 0 : conversion.abi, ntotalargs, rtype.handle, Pointer.nativeValue(atypes_memory));
                    if (handle == 0L) throw new IllegalStateException("Failed to initialize ffi_cif");
                    if (nfixedargs != ntotalargs) {
                        int status = JNIDispatch.ffi_prep_cif_var(handle, JNIDispatch.ffi_get_abi(handle),
                                nfixedargs, ntotalargs, rtype.handle, Pointer.nativeValue(atypes_memory));
                        if (JNIDispatch.ffi_error("ffi_prep_cif_var", status))
                            throw new IllegalStateException("Failed to initialize ffi_cif");
                    }
                    CallContext context = new CallContext(handle, atypes_memory,
                            conversion == null ? CallingConvention.CDECL
                                    : (conversion.abi == 0 ? CallingConvention.CDECL : CallingConvention.STDCALL), rtype, atypes);
                    Cleaner.getCleaner().register(context, () -> Native.free(handle));
                    CIF_MAP.put(boxed, context);
                }
            }
        }
        return CIF_MAP.get(boxed);
    }

    public static CallContext getCallContext(CallingConvention convention, Type rtype, Type[] atypes, int nfixedargs, int ntotalargs) {
        return getCallContext(convention, rtype, atypes, 0, nfixedargs, ntotalargs);
    }

    public static CallContext getCallContext(CallingConvention convention, Type rtype, Type[] atypes, int nfixedargs) {
        return getCallContext(convention, rtype, atypes, 0, nfixedargs, atypes.length);
    }

    public static CallContext getCallContext(CallingConvention convention, Type rtype, Type... atypes) {
        return getCallContext(convention, rtype, atypes, 0, -1, atypes.length);
    }

    public static CallContext getCallContext(Type rtype, Type[] atypes, int atypesOffset, int nfixedargs, int ntotalargs) {
        return getCallContext(null, rtype, atypes, atypesOffset, nfixedargs, ntotalargs);
    }

    public static CallContext getCallContext(Type rtype, Type[] atypes, int nfixedargs) {
        return getCallContext(null, rtype, atypes, nfixedargs);
    }

    public static CallContext getCallContext(Type rtype, Type... atypes) {
        return getCallContext(null, rtype, atypes);
    }

    final long handle;
    private final Memory atypes_memory;
    private final CallingConvention convention;
    final Type rtype;
    final Type[] atypes;

    private CallContext(long handle, Memory atypes_memory, CallingConvention convention, Type rtype, Type[] atypes) {
        this.handle = handle;
        this.atypes_memory = atypes_memory;
        this.convention = convention;
        this.rtype = rtype;
        this.atypes = atypes;
    }

    public CallingConvention getCallingConvention() {
        return convention;
    }

    public Type getReturnType() {
        return rtype;
    }

    public Type[] getParameterTypes() {
        return atypes.clone();
    }

    public int getParameterCount() {
        return atypes.length;
    }

    public Type getParameterType(int index) {
        return atypes[index];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (convention == CallingConvention.STDCALL) {
            builder.append("stdcall");
            builder.append(' ');
        }
        else {
            builder.append("cdecl");
            builder.append(' ');
        }
        Type returnType = getReturnType();
        builder.append(returnType == null ? "void" : returnType);
        builder.append(' ');
        builder.append('(').append('*').append(')');
        builder.append('(');
        if (atypes.length > 0) {
            builder.append(atypes[0]);
            for (int i = 1; i < atypes.length; i ++) {
                builder.append(',').append(' ').append(atypes[i]);
            }
        }
        builder.append(')');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallContext cif = (CallContext) o;
        return handle == cif.handle;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(handle);
    }
    
}
