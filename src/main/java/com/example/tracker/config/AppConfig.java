package com.example.tracker.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
    
    @Bean
    CloseableHttpClient closeableHttpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
