package com.wk.boot.client;

import com.wk.boot.util.Msg;
import com.wk.boot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaige on 2017/4/7.
 */

@Component
public class RestClient {

    private ApiRestTemplate apiRestTemplate;

//    @Autowired
//    public RestClient(ApiRestTemplate apiRestTemplate) {
//        this.apiRestTemplate = apiRestTemplate;
//    }


    @Autowired
    public void setApiRestTemplate(ApiRestTemplate apiRestTemplate) {
        this.apiRestTemplate = apiRestTemplate;
    }

    /**
     * POST请求
     *
     * @param url     请求地址
     * @param params  参数
     * @param headers 请求头
     * @param cls     请求对象Class
     * @param uriVars 请求Url的参数值
     * @param <T>     请求对象类型
     * @return 请求对象
     */
    public <T> T post(String url, Map<String, String> params, Map<String, String> headers, Class<T> cls, Object... uriVars) {

        String body = Util.serializeMap(params);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        if (headers != null) {
            headers.keySet().forEach(key -> multiValueMap.add(key, headers.get(key)));
        }
        applyCommonHeaders(multiValueMap);

        HttpEntity<String> entity = new HttpEntity<>(body, multiValueMap);
        Msg msg = apiRestTemplate.postForObject(url, entity, Msg.class, uriVars);

        validateResponseMsg(msg, Map.class);

        return Util.transfer((Map<String, Object>) msg.getObj(), cls);
    }

    private void validateResponseMsg(Msg msg, Class cls) {
        if (msg == null) {
            throw new RestClientException("no data response");
        }
        if (!msg.isSuccess()) {
            throw new RestClientException(msg.getErrmsg());
        }
        if (cls != null && msg.getObj() != null && !(cls.isAssignableFrom(msg.getObj().getClass()))) {
            throw new RestClientException("response data is not a {" + cls + "}");
        }
    }

    /**
     * GET 请求
     */
    public <T> T get(String url, Class<T> cls, Object... uriVars) {

        Msg msg = apiRestTemplate.getForObject(url, Msg.class, uriVars);

        validateResponseMsg(msg, Map.class);

        return Util.transfer((Map<String, Object>) msg.getObj(), cls);
    }

    /**
     * GET 请求，请求一个列表
     */
    public <T> List<T> getForList(String url, Class<T> cls, Object... uriVars) {

        Msg msg = apiRestTemplate.getForObject(url, Msg.class, uriVars);

        validateResponseMsg(msg, List.class);

        List list = (List) msg.getObj();
        List<T> entities = new ArrayList<T>();
        for (Object o : list) {
            entities.add(Util.transfer((Map<String, Object>) o, cls));
        }

        return entities;
    }

//    public void put(String url, Map<String, String> params, Map<String, String> headers, Object... uriVars) {
//
//        String body = Util.serializeMap(params);
//
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        if (headers != null) {
//            headers.keySet().forEach(key -> multiValueMap.add(key, headers.get(key)));
//        }
//        applyCommonHeaders(multiValueMap);
//
//        HttpEntity<String> entity = new HttpEntity<>(body, multiValueMap);
//        Msg msg = apiRestTemplate.putForObject(url, entity, Msg.class, uriVars);
//
//        validateResponseMsg(msg, null);
//    }

    /**
     * DELETE 请求
     */
    public void delete(String url, Object... uriVars) {

        Msg msg = apiRestTemplate.deleteForObject(url, null, Msg.class, uriVars);

        validateResponseMsg(msg, null);
    }

    private void applyCommonHeaders(MultiValueMap<String, String> multiValueMap) {
        addIfAbsent(multiValueMap, "Accept", "application/json, text/javascript, */*; q=0.01");
        addIfAbsent(multiValueMap, "X-Requested-With", "XMLHttpRequest");
        addIfAbsent(multiValueMap, "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    }

    private void addIfAbsent(MultiValueMap<String, String> multiValueMap, String key, String value) {

        if (!multiValueMap.containsKey(key)) {
            multiValueMap.add(key, value);
        }
    }

}
