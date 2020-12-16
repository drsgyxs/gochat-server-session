package com.drsg.gochat.v1.entity;

import com.drsg.gochat.v1.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.CredentialsContainer;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author YXs
 */
public class Room extends BaseEntity implements CredentialsContainer {
    @Id
    @KeySql(sql = "select SEQ_ROOM.nextval from dual", order = ORDER.BEFORE)
    private Long roomId;
    private String roomName;
    private String notice;
    private Boolean isPrivate;
    private String password;
    private Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Transient
    private UserInfo owner;
    @Transient
    private Set<UserInfo> onlineUsers;
    @Transient
    private Integer onlineCount;

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public Room setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
        return this;
    }

    public Set<UserInfo> getOnlineUsers() {
        return onlineUsers;
    }

    public Room setOnlineUsers(Set<UserInfo> onlineUsers) {
        this.onlineUsers = onlineUsers;
        return this;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Room setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public String getRoomName() {
        return roomName;
    }

    public Room setRoomName(String roomName) {
        this.roomName = roomName;
        return this;
    }

    public String getNotice() {
        return notice;
    }

    public Room setNotice(String notice) {
        this.notice = notice;
        return this;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public Room setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Room setPassword(String password) {
        this.password = password;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Room setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Room setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public UserInfo getOwner() {
        return owner;
    }

    public Room setOwner(UserInfo owner) {
        this.owner = owner;
        return this;
    }
}
