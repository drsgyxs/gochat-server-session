package com.drsg.gochat.v1.config;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YXs
 */
@Component
public class OnlineUsers {
    private Map<Long, Map<String, Long>> onlineUsers = new ConcurrentHashMap<>();

    public Map<Long, Map<String, Long>> getOnlineUsers() {
        return onlineUsers;
    }


    public int getOnlineUsersCount() {
        AtomicInteger count = new AtomicInteger(0);
        onlineUsers.forEach((roomId, usersMap) -> count.addAndGet(usersMap.size()));
        return count.get();
    }

    public Map<String, Long> getOnlineUsersByRoom(Long roomId) {
        return onlineUsers.get(roomId);
    }

    public int getOnlineUsersCountByRoom(Long roomId) {
        if (!onlineUsers.containsKey(roomId)) {
            return 0;
        }
        return getOnlineUsersByRoom(roomId).size();
    }

    public void removeUser(String sessionId) {
        for (Map<String, Long> usersMap: onlineUsers.values()) {
            if (usersMap != null && usersMap.containsKey(sessionId)) {
                usersMap.remove(sessionId);
                break;
            }
        }
    }

}
