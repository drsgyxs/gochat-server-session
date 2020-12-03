package com.drsg.gochat.v1.entity;

import com.drsg.gochat.v1.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author YXs
 * @since 2020-11-12
 */
public class UserInfo extends BaseEntity implements UserDetails, CredentialsContainer {
    @Id
    @KeySql(sql = "select SEQ_USER_INFO.nextval from dual", order = ORDER.BEFORE)
    private Long userId;

    private String username;

    private String password;

    private String avatarUrl;

    private String sex;

    private String signature;

    private Boolean enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String channel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime birthday;

    private String nickname;

    private String email;

    private Boolean emailVerified;

    @Transient
    private Set<RoleInfo> roles;

    public Set<RoleInfo> getRoles() {
        return roles;
    }

    public UserInfo setRoles(Set<RoleInfo> roles) {
        this.roles = roles;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserInfo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    public UserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserInfo setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public UserInfo setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public UserInfo setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UserInfo setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public UserInfo setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public UserInfo setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public UserInfo setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public UserInfo setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
