package com.drsg.gochat.v1.config;

import com.drsg.gochat.v1.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YXs
 */
@Component
public class WebSocketEventListener {
    private final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final OnlineUsers onlineUsers;
    private final String ROOM_DEST_PREFIX = "/topic/room";

    public WebSocketEventListener(OnlineUsers onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        logger.info("User connected.");
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        Principal principal = headerAccessor.getUser();
        logger.info("{}被订阅", destination);
        if (destination != null && destination.startsWith(ROOM_DEST_PREFIX)) {
            Long roomId = Long.valueOf(destination.substring(destination.lastIndexOf("/") + 1));
            Long userId = -1L;
            Map<String, Long> users;
            // login user
            if (principal != null) {
                UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
                userId = ((UserInfo) user.getPrincipal()).getUserId();
            }
            Map<Long, Map<String, Long>> onlineUsers = this.onlineUsers.getOnlineUsers();
            if (onlineUsers.containsKey(roomId)) {
                users = onlineUsers.get(roomId);
                users.put(headerAccessor.getSessionId(), userId);
            } else {
                users = new ConcurrentHashMap<>(10);
                users.put(headerAccessor.getSessionId(), userId);
                onlineUsers.put(roomId, users);
            }
        }
        logger.info("当前在线用户数：{}", this.onlineUsers.getOnlineUsersCount());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("User disconnected.");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        this.onlineUsers.removeUser(headerAccessor.getSessionId());
        logger.info("当前在线用户数：{}", this.onlineUsers.getOnlineUsersCount());
    }
}
