package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.config.MessageBody;
import com.drsg.gochat.v1.config.OnlineUsers;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUsers onlineUsers;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, OnlineUsers onlineUsers) {
        this.messagingTemplate = messagingTemplate;
        this.onlineUsers = onlineUsers;
    }

    @MessageMapping("/room/{roomId}")
    public MessageBody<String> sendMessage(@Payload MessageBody<String> message, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new RuntimeException("anonymous");
        }
        message.setId(IdUtil.nextId()).setFrom((UserInfo) principal);
        return message;
    }

    @MessageMapping("/chat")
    public void chat(@Payload MessageBody<String> message, Authentication authentication) {
        message.setFrom((UserInfo) authentication.getPrincipal());
        this.messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/position-updates", message);
    }

    @SubscribeMapping("/room/{roomId}")
    public void subscribe(Principal principal, @DestinationVariable("roomId") Long roomId) {
        Map<String, Long> users = this.onlineUsers.getUsers();
        System.out.println("hello");
        users.put(principal.getName(), roomId);
    }
}
