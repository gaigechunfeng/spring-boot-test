package com.wk.boot.util;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;

/**
 * Created by gaige on 2017/4/7.
 */
public abstract class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
    private static final String PWD_ENCODING = "utf8";

//    public static String getEntityTableName(Class<?> cls) {
//
//        Table entity = cls.getAnnotation(Table.class);
//        if (entity != null) {
//            return entity.name();
//        }
//        throw new RuntimeException("class not annotated by Table");
//    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static String serializeMap(Map<String, String> params) {

        if (params == null) return null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        if (sb.length() > 0) {
            sb.delete(0, 1);
        }
        return sb.toString();
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
                            LOGGER.warn("not field for map key {}", key);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("set field {} value {} error", key, val, e);
                }
            }

            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> Method findSetterMethod(String fieldName, Class<T> cls) {

        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        Method[] methods = cls.getMethods();
        for (Method method : methods) {

            if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }

    private static Object determineValue(Class<?> type, Object val) {

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

    public static String md5(String password) {

        try {
            return DigestUtils.md5DigestAsHex(password.getBytes(PWD_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("md5 error", e);
        }
    }

    public static <T extends Enum> T findEnumByName(Class<T> cls, String name) {

        T[] ts = cls.getEnumConstants();
        for (T t : ts) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static CloseableHttpClient acceptsUntrustedCertsHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLContext context = SSLContextBuilder
                .create()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();
        builder.setSSLContext(context);

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);

        builder.setConnectionManager(connectionManager);

        return builder.build();
    }
}
