package com.duboribu.ecommerce.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Collections;

public class RestUtil {
    private static final RestTemplate restTemplate = getRestTemplate();

    private static RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    public static <T> ResponseEntity<T> post(String url, Object request, HttpHeaders headers, Class<T> responseType){
        HttpEntity<Object> requestEntity = new HttpEntity<>(convert(request), headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    public static <T> ResponseEntity<T> put(String url, Object request, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(convert(request), headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
    }

    public static <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType);
    }
    public static LinkedMultiValueMap<String, String> convert(Object obj) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        // 객체의 클래스에서 모든 필드 가져오기
        Field[] fields = obj.getClass().getDeclaredFields();
        // 각 필드의 이름과 값을 Map에 추가
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), Collections.singletonList(String.valueOf(field.get(obj))));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }
}