package com.wk.boot.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by 005689 on 2017/4/12.
 */
public class BeanUtil {
    public static Object getFieldValue(Object obj, String fieldName) {

        if (obj == null) throw new NullPointerException();
        Method getterMethod = findGetterMethod(obj.getClass(), fieldName);
        if (getterMethod == null) {
            throw new RuntimeException("cannot find get method of " + fieldName);
        }
        try {
            return getterMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("cannot get field value of " + fieldName);
        }
    }

    private static Method findGetterMethod(Class<? extends Object> cls, String fieldName) {

        Method[] methods = cls.getMethods();

        for (Method method : methods) {

            Class type = method.getReturnType();
            if (type == Void.class) {
                continue;
            }
            boolean isBoolean = type == Boolean.class || type == boolean.class;
            String methodName = findGetterMethodName(fieldName, isBoolean);
            if (method.getName().equals(methodName) && method.getParameterCount() == 0) {
                return method;
            }
        }
        return null;
    }

    private static String findGetterMethodName(String fieldName, boolean isBoolean) {
        String ext = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String pre = isBoolean ? "is" : "get";

        return pre + ext;
    }

    static boolean isAccessLegalMethod(int modifiers) {
        return Modifier.isPublic(modifiers)
                && !Modifier.isStatic(modifiers)
                && !Modifier.isAbstract(modifiers)
                && !Modifier.isFinal(modifiers)
                && !Modifier.isNative(modifiers);
    }
}
