package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.config.MessageBody;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WebSocketController {
        @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/public")
    public MessageBody sendMessage(@Payload MessageBody message, Principal principal) {
        System.out.println("Received a message");
        System.out.println(principal);
        return message;
    }

    @MessageMapping("/chat/addUser")
    @SendTo("/topic/public")
    public MessageBody addUser(@Payload MessageBody message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        System.out.println("A user joined");
        return message;
    }
}
