package io.github.fasterjna.jna.ffi;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public final class Memory {

    private Memory() {
        throw new AssertionError("No io.github.fasterjna.jna.ffi.Memory instances for you!");
    }

    public static native Pointer memcpy(Pointer destAddress, Pointer srcAddress, int size);
    static {
        Native.register(Memory.class, Platform.C_LIBRARY_NAME);
    }

}
