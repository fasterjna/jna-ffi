package jna.ffi;

import com.sun.jna.Native;
import com.sun.jna.internal.Cleaner;

import java.util.Map;
import java.util.WeakHashMap;

import static com.sun.jna.io_github_fasterjna_jna_ffi_ffi_type.*;

public final class Type {

    private static final Map<Integer, Type> SINT8_ARRAY_MAP = new WeakHashMap<>();
    public static Type getSInt8Array(int size) {
        Integer boxed = size;
        if (!SINT8_ARRAY_MAP.containsKey(boxed)) {
            synchronized (SINT8_ARRAY_MAP) {
                if (!SINT8_ARRAY_MAP.containsKey(boxed)) {
                    long handle = ffi_type_sint8_array_new(size);
                    SINT8_ARRAY_MAP.put(boxed, new Type(handle, size, true));
                }
            }
        }
        return SINT8_ARRAY_MAP.get(boxed);
    }

    public static final Type VOID = new Type(ffi_type_void, 0);
    public static final Type SINT8 = new Type(ffi_type_sint8, 1);
    public static final Type SINT16 = new Type(ffi_type_sint16, 2);
    public static final Type JCHAR = new Type(ffi_type_sint16, 2);
    public static final Type SINT32 = new Type(ffi_type_sint32, 4);
    public static final Type SINT64 = new Type(ffi_type_sint64, 8);
    public static final Type FLOAT = new Type(ffi_type_float, 4);
    public static final Type DOUBLE = new Type(ffi_type_double, 8);
    public static final Type POINTER = new Type(ffi_type_pointer, Native.POINTER_SIZE);
    public static final Type LONG = new Type((Native.LONG_SIZE == 4L ? SINT32 : SINT64).handle, Native.LONG_SIZE);
    public static final Type SIZE = new Type((Native.SIZE_T_SIZE == 4L ? SINT32 : SINT64).handle, Native.SIZE_T_SIZE);
    public static final Type BOOLEAN = new Type(POINTER.handle, POINTER.size);
    public static final Type WCHAR = new Type(ffi_type_wchar, Native.WCHAR_SIZE);

    final long handle;
    final int size;
    final boolean compound;

    private Type(long handle, int size) {
        this(handle, size, false);
    }

    private Type(long handle, int size, boolean compound) {
        this.handle = handle;
        this.size = size;
        this.compound = compound;
        Cleaner.getCleaner().register(this, () -> ffi_type_sint8_array_delete(handle));
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;
        return size == type.size;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(handle);
    }

}
