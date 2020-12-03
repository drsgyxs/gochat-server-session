package com.drsg.gochat.v1.config;

import com.drsg.gochat.v1.entity.Room;
import com.drsg.gochat.v1.exception.BusinessException;
import com.drsg.gochat.v1.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketEventListener {
    private final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final OnlineUsers onlineUsers;
    private final RoomService roomService;
    private final SimpMessageSendingOperations messageSendingOperations;

    public WebSocketEventListener(OnlineUsers onlineUsers, RoomService roomService, SimpMessageSendingOperations messageSendingOperations) {
        this.onlineUsers = onlineUsers;
        this.roomService = roomService;
        this.messageSendingOperations = messageSendingOperations;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        Principal user = event.getUser();
        logger.info("User connected.");
        if (user == null) {
            AtomicInteger anonymousCount = this.onlineUsers.getAnonymousCount();
            int i = anonymousCount.incrementAndGet();
            logger.info("当前在线匿名用户数：{}", i);
        }
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        List<String> passcode = headerAccessor.getNativeHeader("passcode");
        String password = null;
        if (passcode != null && !passcode.isEmpty())
            password = passcode.get(0);
        String destination = headerAccessor.getDestination();
        if (destination == null)
            throw new BusinessException("订阅出错");
        if (destination.startsWith("/topic/room")) {
            Long roomId = Long.valueOf(destination.substring(destination.lastIndexOf("/") + 1));
            Room room = this.roomService.getRoomById(roomId, password);
            if (room == null) {
                throw new BusinessException("密码错误");
            }
        }
        logger.info("{}被订阅", destination);
    }



    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        logger.info("User disconnected.");
        if (user == null) {
            AtomicInteger anonymousCount = this.onlineUsers.getAnonymousCount();
            anonymousCount.decrementAndGet();
        } else {
            this.onlineUsers.getUsers().remove(user.getName());
        }
        logger.info("当前在线用户数：{}，当前在线匿名用户数：{}", this.onlineUsers.getUsers().size(), this.onlineUsers.getAnonymousCount().get());
    }
}
