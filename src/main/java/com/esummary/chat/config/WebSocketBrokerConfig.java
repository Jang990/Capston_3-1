package com.esummary.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue", "/topic"); // queue 또는 topic이라는 prefix가 붙은 메시지를 브로커가 처리
		// queue는 1:1, topic은 1:N일 때 주로 사용
		
		registry.setApplicationDestinationPrefixes("/"); // 메시지의 처리나 가공이 필요할 때 핸들러를 통할 때 메시지앞에 ...을 붙이면 된다.
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/chat")
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}
}
