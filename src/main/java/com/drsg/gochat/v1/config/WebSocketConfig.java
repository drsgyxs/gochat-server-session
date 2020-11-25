package com.drsg.gochat.v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry
                .enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(taskScheduler())
                .setHeartbeatValue(new long[] {10000, 0});
    }

    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("WebSocket-heart-beat-thread-");
        taskScheduler.initialize();
        return taskScheduler;
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                assert accessor != null;
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    System.out.println(accessor.getLogin() + ": " + accessor.getPasscode());
//                    Authentication user = SecurityContextHolder.getContext().getAuthentication();
//                    System.out.println(user);
//                    System.out.println(accessor);
//                    accessor.setUser(user);
//                }
//                return message;
//            }
//        });
//    }

    //    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//        webSocketHandlerRegistry.addHandler(myHandler(), "/ws").setAllowedOrigins("*");
//    }
//
//    @Bean
//    public WebSocketHandler myHandler() {
//        return new MyHandler();
//    }
//
//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(8192);
//        container.setMaxBinaryMessageBufferSize(8192);
//        container.setMaxSessionIdleTimeout(600_000L);
//        return container;
//    }
}
