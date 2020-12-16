package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.config.MessageBody;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YXs
 */
@RestController
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
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

    @MessageExceptionHandler
    public MessageBody<String> handleNoLoginException() {
        MessageBody<String> msg = new MessageBody<>();
        msg.setContent("登录后才可以发言哦！");
        return msg;
    }
}
