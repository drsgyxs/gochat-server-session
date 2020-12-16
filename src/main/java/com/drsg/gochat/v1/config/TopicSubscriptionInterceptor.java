package com.drsg.gochat.v1.config;

import com.drsg.gochat.v1.entity.Room;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.exception.BusinessException;
import com.drsg.gochat.v1.service.RoomService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author YXs
 */

@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {
    private final RoomService roomService;
    private final String ROOM_DEST_PREFIX = "/topic/room";

    public TopicSubscriptionInterceptor(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            throw new BusinessException("订阅出错");
        }
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            List<String> passcode = accessor.getNativeHeader("passcode");
            String password = null;
            if (passcode != null && !passcode.isEmpty()) {
                password = passcode.get(0);
            }
            String destination = accessor.getDestination();
            if (destination == null) {
                throw new BusinessException("订阅出错");
            }
            if (destination.startsWith(ROOM_DEST_PREFIX)) {
                Long roomId = Long.valueOf(destination.substring(destination.lastIndexOf("/") + 1));
                Room room = this.roomService.selectByRoomId(roomId, password);
                if (room == null) {
                    throw new BusinessException("密码错误");
                }
            }
        }
        return message;
    }
}
