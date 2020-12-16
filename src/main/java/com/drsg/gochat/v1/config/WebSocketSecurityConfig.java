package com.drsg.gochat.v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @author YXs
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // 发言需要登录
                .simpDestMatchers("/app/**").hasRole("USER")
                .simpSubscribeDestMatchers("/topic/**").permitAll()
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll()
                //私聊需登录
//                .simpSubscribeDestMatchers("/user/**").authenticated()
                // 不允许直接发送消息到代理
                .simpMessageDestMatchers("/topic/**", "/queue/**").denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
