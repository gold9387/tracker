package com.example.tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커의 옵션을 구성합니다.
     * "/topic"를 사용하여 Simple Broker를 활성화하고, 애플리케이션의 prefix로 "/app"을 설정합니다.
     * 
     * @param config 메시지 브로커 구성 레지스트리
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * STOMP 엔드포인트를 등록합니다.
     * 클라이언트가 웹소켓 연결을 초기화할 때 사용하는 엔드포인트를 "/ws"로 설정합니다.
     * 
     * @param registry STOMP 엔드포인트를 등록할 레지스트리
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
