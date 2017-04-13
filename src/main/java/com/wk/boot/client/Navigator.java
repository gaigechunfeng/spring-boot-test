package com.wk.boot.client;

import com.wk.boot.constant.Error;
import com.wk.boot.util.Msg;
import com.wk.boot.util.ReflectionUtil;
import com.wk.boot.util.Util;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaige on 2017/4/11.
 */
public class Navigator {

    private static final String DEFAULT_PAGE_NAME = "default_web_page";
    private static final Map<String, WebPage> webPageMap = new ConcurrentHashMap<>();
    private static Navigator INS;
    private RestTemplate restTemplate;

    private Navigator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static Navigator instance(RestTemplate restTemplate) {

        if (INS == null) {
            synchronized (Navigator.class) {
                if (INS == null) {
                    INS = new Navigator(restTemplate);
                }
                return INS;
            }
        } else {
            return INS;
        }
    }

    public WebPage defaultWebPage() {
        return getWebPage(DEFAULT_PAGE_NAME);
    }

    public WebPage getWebPage(String pageName) {
        return webPageMap.computeIfAbsent(pageName, k -> new WebPage(restTemplate));
    }

    public static class WebPage extends RestTemplate {

        private RestTemplate restTemplate;
//        private Map<String, List<String>> headers = new ConcurrentHashMap<>();


        private WebPage(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public <T> T post(String url, Map<String, String> params, Class<T> cls, Object... pathVars) {

            return request(url, HttpMethod.POST, params, cls, pathVars);
        }

        public <T> T get(String url, Map<String, String> params, Class<T> cls, Object... pathVars) {

            return request(url, HttpMethod.GET, params, cls, pathVars);
        }

        public <T> T put(String url, Map<String, String> params, Class<T> cls, Object... pathVars) {

            return request(url, HttpMethod.PUT, params, cls, pathVars);
        }

        public <T> T delete(String url, Map<String, String> params, Class<T> cls, Object... pathVars) {

            return request(url, HttpMethod.DELETE, params, cls, pathVars);
        }

        /**
         * 请求服务
         *
         * @param url          请求地址
         * @param method       请求方法 GET POST 等
         * @param params       请求参数
         * @param cls          请求数据class
         * @param uriVariables url 占位
         * @param <T>          T
         * @return 请求数据
         */
        public <T> T request(String url, HttpMethod method, Map<String, String> params, Class<T> cls, Object... uriVariables) {

            //prepare request header
//            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(headers);
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("Referer", url);
            applyCommonHeaders(multiValueMap);
            if (HttpMethod.POST.equals(method)) {
                addIfAbsent(multiValueMap, "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            }
            HttpEntity<String> request = new HttpEntity<>(Util.serializeMap(params), multiValueMap);

            //do request
            Class responseType = Msg.class;
            RequestCallback requestCallback = httpEntityCallback(request, responseType);
            ResponseExtractor<ResponseEntity<Msg>> responseExtractor = responseEntityExtractor(responseType);
            ResponseEntity<Msg> responseEntity = restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables);

            //parse response cookie
//            parseResponseHeader(responseEntity.getHeaders());

            //get msg object
            Msg msg = responseEntity.getBody();

            //validate response msg
            validateResponseMsg(msg, Map.class);

            //generate final result and return
            return msg.getObj() == null || cls == null ? null : ReflectionUtil.transfer((Map<String, Object>) msg.getObj(), cls);
        }

//        private void parseResponseHeader(HttpHeaders httpHeaders) {
//            List<String> cookies = httpHeaders.get("Set-Cookie");
//            if (!Util.isEmpty(cookies)) {
//                headers.put("Cookie", cookies);
//            }
//        }

//        public void close() {
//            headers.clear();
//        }

    }

    private static void validateResponseMsg(Msg msg, Class cls) {
        if (msg == null) {
            throw new RestClientException("no data response");
        }
        if (!msg.isSuccess()) {
            Error error = Util.findEnumByName(Error.class, msg.getErrmsg());
            throw new RestClientException(error == null ? msg.getErrmsg() : error.getErrMsg());
        }
        if (cls != null && msg.getObj() != null && !(cls.isAssignableFrom(msg.getObj().getClass()))) {
            throw new RestClientException("response data is not a {" + cls + "}");
        }
    }

    private static void applyCommonHeaders(MultiValueMap<String, String> multiValueMap) {
        addIfAbsent(multiValueMap, "Accept", "application/json, text/javascript, */*; q=0.01");
        addIfAbsent(multiValueMap, "X-Requested-With", "XMLHttpRequest");
    }

    private static void addIfAbsent(MultiValueMap<String, String> multiValueMap, String key, String value) {

        if (!multiValueMap.containsKey(key)) {
            multiValueMap.add(key, value);
        }
    }
}
