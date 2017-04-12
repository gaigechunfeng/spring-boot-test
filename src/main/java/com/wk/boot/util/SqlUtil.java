package com.wk.boot.util;

import com.wk.boot.annotation.Entity;
import com.wk.boot.model.IdEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 005689 on 2017/4/12.
 */
public class SqlUtil {
    public static <T extends IdEntity> SqlInfo genAddSql(T t) {

        if (t == null) throw new NullPointerException();
        List<String> fieldName = searchFieldName(t);
        if (Util.isCollectionEmpty(fieldName)) {
            throw new RuntimeException("no fields found in " + t.getClass());
        }
        List<Object> params = new ArrayList<>();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        fieldName.stream().filter(s -> !"id".equalsIgnoreCase(s)).forEach(s -> {
            sb1.append(",").append(s);
            sb2.append(",?");
            params.add(BeanUtil.getFieldValue(t, s));
        });
        if (sb1.length() > 0) sb1.delete(0, 1);
        if (sb2.length() > 0) sb2.delete(0, 1);

        String tableName = getTableName(t.getClass());
        return new SqlInfo("insert into " + tableName + "(" + sb1 + ") values(" + sb2 + ")", params);
    }

    private static List<String> searchFieldName(Object o) {

        Class cls = o.getClass();
        Method[] ms = cls.getMethods();
        List<String> fieldNames = new ArrayList<>();
        for (Method m : ms) {
            String methodName = m.getName();

            boolean isAccessLegal = BeanUtil.isAccessLegalMethod(m.getModifiers());
            if (!isAccessLegal) continue;

            Class type = m.getReturnType();
            boolean typeIsBoolean = type == Boolean.class || type == boolean.class;
            boolean nameMatch = (typeIsBoolean && methodName.startsWith("is")) || (!typeIsBoolean && methodName.startsWith("get"));
            if (nameMatch) {
                String mn = typeIsBoolean ? methodName.substring(2) : methodName.substring(3);
                if (mn.length() > 0) {
                    mn = mn.substring(0, 1).toLowerCase() + mn.substring(1);
                    fieldNames.add(mn);
                }
            }
        }
        return fieldNames;
    }

    public static <T extends IdEntity> SqlInfo genEditSql(T t) {
        if (t == null) throw new NullPointerException();
        List<String> fieldName = searchFieldName(t);
        if (Util.isCollectionEmpty(fieldName)) {
            throw new RuntimeException("no fields found in " + t.getClass());
        }
        List<Object> params = new ArrayList<>();
        StringBuilder sb1 = new StringBuilder();
        fieldName.stream().filter(s -> !"id".equalsIgnoreCase(s)).forEach(s -> {
            sb1.append(",").append(s).append("=?");
            params.add(BeanUtil.getFieldValue(t, s));
        });
        sb1.append(" where id=?");
        params.add(t.getId());
        if (sb1.length() > 0) sb1.delete(0, 1);

        String tableName = getTableName(t.getClass());
        return new SqlInfo("update " + tableName + " set " + sb1, params);
    }

    public static <T> String getTableName(Class<T> cls) {

        Entity entity = cls.getAnnotation(Entity.class);
        if (entity != null) {
            return entity.tableName();
        }
        return null;
    }

    /**
     * Created by 005689 on 2017/4/12.
     */
    public static final class SqlInfo {

        private final String sql;
        private final List<Object> params;

        public SqlInfo(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getParams() {
            return params;
        }
    }
}
