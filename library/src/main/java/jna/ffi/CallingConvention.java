package jna.ffi;

import com.sun.jna.Platform;

public enum CallingConvention {

    CDECL(0),
    STDCALL((Platform.isWindows() && !Platform.isWindowsCE() && !Platform.is64Bit()) ? 2 : 0);

    final int abi;
    CallingConvention(int abi) {
        this.abi = abi;
    }

}
