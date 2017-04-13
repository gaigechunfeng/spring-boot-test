package com.wk.boot.util;

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
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

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

    public static boolean isStringEmpty(String tn) {
        return tn == null || "".equals(tn.trim());
    }

    public static boolean isCollectionEmpty(Collection co) {
        return co == null || co.size() == 0;
    }

    public static String encodePwd(String pwd) {
        if (pwd == null) throw new NullPointerException();
        try {
            return org.apache.commons.codec.digest.DigestUtils.sha256Hex(pwd.getBytes(PWD_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode password error", e);
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
