//package com.wk.boot.client;
//
//import org.springframework.http.HttpMethod;
//import org.springframework.web.client.HttpMessageConverterExtractor;
//import org.springframework.web.client.RequestCallback;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
///**
// * Created by gaige on 2017/4/10.
// */
//public class ApiRestTemplate extends RestTemplate {
//
//    public <T> T putForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
//            throws RestClientException {
//
//        RequestCallback requestCallback = httpEntityCallback(request, responseType);
//        HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters());
//        return execute(url, HttpMethod.PUT, requestCallback, responseExtractor, uriVariables);
//    }
//
//    public <T> T deleteForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
//            throws RestClientException {
//
//        RequestCallback requestCallback = httpEntityCallback(request, responseType);
//        HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters());
//        return execute(url, HttpMethod.DELETE, requestCallback, responseExtractor, uriVariables);
//    }
//
//}
