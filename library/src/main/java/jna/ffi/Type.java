package jna.ffi;

import com.sun.jna.Native;
import com.sun.jna.internal.Cleaner;

import java.util.Map;
import java.util.WeakHashMap;

import static com.sun.jna.io_github_fasterjna_jna_ffi_ffi_type.*;

public final class Type {

    private static final Map<Integer, Type> SINT8_ARRAY_MAP = new WeakHashMap<>();
    public static Type ofSInt8Array(int size) {
        Integer boxed = size;
        if (!SINT8_ARRAY_MAP.containsKey(boxed)) {
            synchronized (SINT8_ARRAY_MAP) {
                if (!SINT8_ARRAY_MAP.containsKey(boxed)) {
                    long handle = ffi_type_sint8_array_new(size);
                    Type type = new Type(handle, size, true, "struct");
                    Cleaner.getCleaner().register(type, () -> ffi_type_sint8_array_delete(handle));
                    SINT8_ARRAY_MAP.put(boxed, type);
                }
            }
        }
        return SINT8_ARRAY_MAP.get(boxed);
    }

    public static final Type VOID = new Type(ffi_type_void, 0, false, "void");
    public static final Type SINT8 = new Type(ffi_type_sint8, 1, false, "int8_t");
    public static final Type SINT16 = new Type(ffi_type_sint16, 2, false, "int16_t");
    public static final Type UNICHAR = new Type(ffi_type_sint16, 2, false, "unichar");
    public static final Type SINT32 = new Type(ffi_type_sint32, 4, false, "int32_t");
    public static final Type SINT64 = new Type(ffi_type_sint64, 8, false, "int64_t");
    public static final Type FLOAT = new Type(ffi_type_float, 4, false, "float");
    public static final Type DOUBLE = new Type(ffi_type_double, 8, false, "double");
    public static final Type POINTER = new Type(ffi_type_pointer, Native.POINTER_SIZE, false, "caddr_t");
    public static final Type SLONG = new Type((Native.LONG_SIZE == 4L ? SINT32 : SINT64).handle, Native.LONG_SIZE, false, "long");
    public static final Type SIZE = new Type((Native.SIZE_T_SIZE == 4L ? SINT32 : SINT64).handle, Native.SIZE_T_SIZE, false, "size_t");
    public static final Type BOOLEAN = new Type(POINTER.handle, POINTER.size, false, "boolean");
    public static final Type WCHAR = new Type(ffi_type_wchar, Native.WCHAR_SIZE, false, "wchar_t");

    final long handle;
    final int size;
    final boolean compound;

    private final String name;

    private Type(long handle, int size, boolean compound, String name) {
        this.handle = handle;
        this.size = size;
        this.compound = compound;
        this.name = name;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return name + ':' + size;
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
