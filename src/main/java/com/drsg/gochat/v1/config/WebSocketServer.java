package com.drsg.gochat.v1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YXs
 */
//@ServerEndpoint("/chat/{roomId}/{userId}")
//@Component
public class WebSocketServer {
    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static final Map<String, Set<Session>> roomMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        Set<Session> userSessions = roomMap.get(roomId);
        if (userSessions == null) {
            userSessions = Collections.synchronizedSet(new HashSet<>());
            userSessions.add(session);
            roomMap.put(roomId, userSessions);
        } else {
            userSessions.add(session);
        }
        addOnlineCount();
        logger.info("用户连接：{}，当前在线人数：{}", userId, onlineCount);

    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId, @PathParam("userId") String userId, Session session) {
        roomMap.get(roomId).remove(session);
        subOnlineCount();
        logger.info("用户退出：{}，当前在线人数：{}", userId, onlineCount);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        if (!StringUtils.isEmpty(message)) {
            roomMap.get(roomId).forEach(userSession -> sendMessage(userSession, message));
        }
        logger.info("{}：{}", userId, message);
    }

    private static void sendMessage(Session session, String message) {
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable e, @PathParam("userId") String userId, @PathParam("roomId") String roomId, Session session) throws IOException {
        logger.info("用户" + userId + "错误，原因：" + e.getMessage());
        e.printStackTrace();
        roomMap.get(roomId).remove(session);
        session.close();
    }

    private synchronized int addOnlineCount() {
        return onlineCount.incrementAndGet();
    }

    private synchronized int subOnlineCount() {
        return onlineCount.decrementAndGet();
    }
}
