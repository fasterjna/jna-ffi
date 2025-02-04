package com.sun.jna;

import java.util.HashMap;
import java.util.Map;

public final class io_github_fasterjna_jna_ffi_ffi_type {

    private io_github_fasterjna_jna_ffi_ffi_type() {
        throw new AssertionError("No com.sun.jna.io_github_fasterjna_jna_ffi_ffi_type instances for you!");
    }

    public static final long ffi_type_void = Structure.getTypeInfo(void.class).getPointer().peer;
    public static final long ffi_type_wchar = Structure.getTypeInfo(char.class).getPointer().peer;
    public static final long ffi_type_sint8 = Structure.getTypeInfo(byte.class).getPointer().peer;
    public static final long ffi_type_sint16 = Structure.getTypeInfo(short.class).getPointer().peer;
    public static final long ffi_type_sint32 = Structure.getTypeInfo(int.class).getPointer().peer;
    public static final long ffi_type_sint64 = Structure.getTypeInfo(long.class).getPointer().peer;
    public static final long ffi_type_float = Structure.getTypeInfo(float.class).getPointer().peer;
    public static final long ffi_type_double = Structure.getTypeInfo(double.class).getPointer().peer;
    public static final long ffi_type_pointer = Structure.getTypeInfo(Pointer.class).getPointer().peer;

    private static final Map<Integer, Structure.FFIType> typeMap = new HashMap<>();
    public static long ffi_type_sint8_array_new(int length) {
        Integer boxed = length;
        if (!typeMap.containsKey(boxed)) synchronized (typeMap) {
            if (!typeMap.containsKey(boxed)) {
                Structure.FFIType type = new Structure.FFIType();
                Pointer pointer = Structure.FFIType.getTypeInfo(byte.class).getPointer();
                Pointer[] pointers = new Pointer[length + 1];
                for (int i = 0; i < length; i ++) {
                    pointers[i] = pointer;
                }
                type.elements = new Memory((long) Native.POINTER_SIZE * pointers.length);
                type.elements.write(0, pointers, 0, pointers.length);
                type.size = new Structure.FFIType.size_t(length);
                type.alignment = 1;
                type.write();
                typeMap.put(boxed, type);
            }
        }
        return typeMap.get(boxed).getPointer().peer;
    }

    public static void ffi_type_sint8_array_delete(long handle) {
        synchronized (typeMap) {
            typeMap.remove(Structure.newInstance(Structure.FFIType.class, new Pointer(handle)).size.intValue());
        }
    }

}
