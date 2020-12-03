package com.drsg.gochat.v1.config;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OnlineUsers {
    private Map<String, Long> users = new ConcurrentHashMap<>();
    private AtomicInteger anonymousCount = new AtomicInteger(0);

    public Map<String, Long> getUsers() {
        return users;
    }

    public AtomicInteger getAnonymousCount() {
        return anonymousCount;
    }

}
