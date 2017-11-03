package kz.azonium.dc;

import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;

public final class DeepCloneUtil {

    private static final Unsafe unsafe;

    static {
        try {
            Constructor<Unsafe> constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            unsafe = constructor.newInstance();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static <T>T clone(T o) throws IllegalAccessException, InstantiationException {
        return clone(null, o);
    }

    private static <T>T clone(Map<Object, Object> ctx, T o) throws InstantiationException, IllegalAccessException {
        if (o == null) return null;
        if (ctx == null) ctx = new HashMap<Object, Object>();
        if (ctx.containsKey(o)) return (T) ctx.get(o);
        if (isPrimitive(o.getClass())) return o;
        if (o.getClass().isArray()) return (T) cloneArray(ctx, (T[])o);

        Class<T> clazz = (Class<T>) o.getClass();
        T copy = (T) unsafe.allocateInstance(clazz);
        ctx.put(o, copy);

        for (Field f : getAllFields(clazz)) {
            if (!Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                f.set(copy, clone(ctx, f.get(o)));
            }
        }

        return copy;
    }

    private static List<Field> getAllFields(Class clazz) {
        List<Field> result = new LinkedList<>();

        while (clazz != null) {
            result.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    private static <T>T[] cloneArray(Map<Object, Object> ctx, T[] array) throws IllegalAccessException, InstantiationException {
        T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        ctx.put(array, copy);

        for (int i = 0; i < array.length; i++) {
            copy[i] = clone(ctx, array[i]);
        }

        return copy;
    }

    private static boolean isPrimitive(Class clazz) {
        if (clazz.isPrimitive()) return true;
        if (clazz.equals(String.class)) return true;
        if (Number.class.isAssignableFrom(clazz)) return true;
        if (clazz.equals(Boolean.class)) return true;
        if (clazz.isEnum()) return true;
        return false;
    }
}
