package io.github.fasterjna.jna.ffi;

import com.sun.jna.Function;
import com.sun.jna.JNIEnv;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public final class JNIDispatch {

    static {
        try {
            Class.forName("com.sun.jna.Native");
        } catch (ClassNotFoundException ignored) {
        }
    }

    private static final Object IMPL_LOOKUP;

    private static final Method unreflectMethod;
    private static final Method invokeWithArgumentsMethod;
    static {
        if (Platform.isAndroid()) {
            IMPL_LOOKUP = null;
            unreflectMethod = null;
            invokeWithArgumentsMethod = null;
        }
        else {
            Unsafe unsafe;
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                unsafe = (Unsafe) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException("Failed to get the sun.misc.Unsafe instance");
            }
            Object _lookup;
            Method _unreflectMethod;
            Method _invokeWithArgumentsMethod;
            try {
                Class<?> lookupClass = Class.forName("java.lang.invoke.MethodHandles$Lookup");
                Field field = lookupClass.getDeclaredField("IMPL_LOOKUP");
                _lookup = unsafe.getObject(lookupClass, unsafe.staticFieldOffset(field));
                _unreflectMethod = lookupClass.getDeclaredMethod("unreflect", Method.class);
                Class<?> methodHandleClass = Class.forName("java.lang.invoke.MethodHandle");
                _invokeWithArgumentsMethod = methodHandleClass.getDeclaredMethod("invokeWithArguments", Object[].class);
            } catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException e) {
                _lookup = null;
                _unreflectMethod = null;
                _invokeWithArgumentsMethod = null;
            }
            IMPL_LOOKUP = _lookup;
            unreflectMethod = _unreflectMethod;
            invokeWithArgumentsMethod = _invokeWithArgumentsMethod;
        }
    }

    private static final Object findNativeMethodHandle;
    static {
        if (Platform.isAndroid()) findNativeMethodHandle = null;
        else {
            try {
                findNativeMethodHandle = unreflectMethod.invoke(IMPL_LOOKUP, ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("Unexpected exception", e);
            }
        }
    }

    private static Function findFunction(String functionName) throws UnsatisfiedLinkError {
        if (Platform.isAndroid()) return NativeLibrary.getProcess().getFunction(functionName);
        else {
            try {
                return Function.getFunction(new Pointer((long) invokeWithArgumentsMethod
                        .invoke(findNativeMethodHandle, (Object) new Object[] { Native.class.getClassLoader(), functionName })));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Unexpected exception", e);
            } catch (InvocationTargetException e) {
                Throwable throwable = e.getTargetException();
                if (throwable instanceof Error) throw (Error) throwable;
                else if (throwable instanceof RuntimeException) throw (RuntimeException) throwable;
                else throw new IllegalStateException("Unexpected exception", throwable);
            }
        }
    }

    private static final Map<String, ?> ALLOW_OBJECTS = Collections.singletonMap(Library.OPTION_ALLOW_OBJECTS, Boolean.TRUE);
    private static final Function ffi_error = findFunction("ffi_error");
    public static boolean ffi_error(String op, int status) {
        return (boolean) ffi_error.invoke(boolean.class, new Object[] { JNIEnv.CURRENT, op, status }, ALLOW_OBJECTS);
    }

    public static int ffi_get_abi(long cif) {
        return new Pointer(cif).getInt(0);
    }

    private static final Function ffi_prep_cif_var = findFunction("ffi_prep_cif_var");
    public static int ffi_prep_cif_var(long cif, int abi, int nfixedargs, int ntotalargs, long rtype, long atypes) {
        return ffi_prep_cif_var.invokeInt(new Object[] { new Pointer(cif), abi, nfixedargs, ntotalargs, new Pointer(rtype), new Pointer(atypes) });
    }
    
}
