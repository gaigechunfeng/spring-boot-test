package com.wk.boot.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by 005689 on 2017/4/12.
 */
public class BeanUtil {

    /**
     * 获取对象的字段值，根据getter方法获取，getter方法名必须根据规范命令，例如：字段名：firstName；getter 方法名：getFirstName <br>
     * 如果字段类型是String，且字段值是null，则返回空格字符串（" "）
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {

        if (obj == null) throw new NullPointerException();
        Method getterMethod = findGetterMethod(obj.getClass(), fieldName);
        if (getterMethod == null) {
            throw new RuntimeException("cannot find get method of " + fieldName);
        }
        try {
            return dealNull(getterMethod.getReturnType(), getterMethod.invoke(obj));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("cannot get field value of " + fieldName);
        }
    }

    private static Object dealNull(Class<?> cls, Object val) {

        if (cls == String.class && val == null) {
            return " ";
        }
        return val;
    }

    private static Method findGetterMethod(Class<?> cls, String fieldName) {

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
