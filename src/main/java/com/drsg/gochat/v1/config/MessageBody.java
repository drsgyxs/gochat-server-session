package com.drsg.gochat.v1.config;

import com.drsg.gochat.v1.entity.UserInfo;

public class MessageBody<T> {
    private Long id;
    private UserInfo from;
    private String to;
    private T content;
    private MessageType type;

    public enum MessageType {
        PUBLIC,
        CHAT,
        JOIN,
        LEAVE
    }

    public Long getId() {
        return id;
    }

    public MessageBody<T> setId(Long id) {
        this.id = id;
        return this;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public UserInfo getFrom() {
        return from;
    }

    public MessageBody<T> setFrom(UserInfo from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
