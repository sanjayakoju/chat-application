package com.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a STOMP over WebSocket endpoint for clients to connect to
        registry.addEndpoint("/chat-socket").setAllowedOrigins("http://localhost:4200").withSockJS();;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple memory-based message broker to send messages to the client
        registry.enableSimpleBroker("/topic");
        // Set the application prefix for messages from the client to start with /app
        registry.setApplicationDestinationPrefixes("/app");
    }
}
