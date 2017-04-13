package com.wk.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by 005689 on 2017/4/12.
 */
public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

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
            throw new RuntimeException("cannot find getterMethod of " + fieldName);
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

    static <T> Method findSetterMethod(String fieldName, Class<T> cls) {

        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        Method[] methods = cls.getMethods();
        for (Method method : methods) {

            if (isAccessLegalMethod(method.getModifiers())
                    && method.getName().equals(methodName)
                    && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }

    static Object determineValue(Class<?> type, Object val) {

        if (val == null) return null;
        if (type == Long.class || type == long.class) {
            return Long.parseLong(val.toString());
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(val.toString());
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(val.toString());
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(val.toString());
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(val.toString());
        } else if (type == String.class) {
            return val.toString();
        } else {
//            throw new RuntimeException("unsupported type {" + type + "}");
            if (val instanceof Map) {
                return transfer((Map<String, Object>) val, type);
            } else {
                throw new RuntimeException("unsupported type {" + type + "}");
            }
        }
    }

    public static <T> T transfer(Map<String, Object> map, Class<T> cls) {

        if (map == null) return null;

        try {
            T t = cls.newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();

                try {
                    Field field = getObjectField(key, cls);
                    if (field != null) {
                        field.set(t, determineValue(field.getType(), val));
                    } else {//如果field是private，则查询setter方法
                        Method setterMethod = findSetterMethod(key, cls);
                        if (setterMethod != null) {
                            setterMethod.invoke(t, determineValue(setterMethod.getParameterTypes()[0], val));
                        } else {
                            LOGGER.warn("not field and setterMethod for map key {}", key);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("set field {} value {} error", key, val, e);
                }
            }

            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("transfer from map to " + cls + " error");
        }
    }

    private static <T> Field getObjectField(String fieldName, Class<T> cls) {

        Field[] fields = cls.getFields();
        for (Field field : fields) {
            if (isLegalField(field) && field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    private static boolean isLegalField(Field field) {

        int m = field.getModifiers();

        return !Modifier.isStatic(m) && !Modifier.isFinal(m);
    }
}
