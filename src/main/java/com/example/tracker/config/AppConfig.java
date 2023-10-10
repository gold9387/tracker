package com.example.tracker.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

    /**
     * 기본 설정을 사용하여 CloseableHttpClient 객체를 생성합니다.
     * 
     * @return CloseableHttpClient 객체
     */
    @Bean
    CloseableHttpClient closeableHttpClient() {
        return HttpClients.createDefault();
    }

    /**
     * Jackson 라이브러리를 사용하여 ObjectMapper 객체를 생성합니다.
     * 이 객체는 JSON 형식의 데이터를 자바 객체로 변환하거나 그 반대로 변환하는 데 사용됩니다.
     * 
     * @return ObjectMapper 객체
     */
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
