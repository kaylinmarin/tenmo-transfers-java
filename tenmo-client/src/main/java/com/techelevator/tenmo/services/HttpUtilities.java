package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpUtilities {
    public static <T> HttpEntity<T> createEntity(String authToken, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<T>(body, headers);
    }

    public static HttpEntity<Void> createEntity(String authToken) {
        return createEntity(authToken, null);
    }
}
