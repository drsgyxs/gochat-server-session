package com.drsg.gochat.v1.entity;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author YXs
 * @since 2020-11-12
 */
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1985119488009188417L;
    @Id
    @KeySql(sql = "select SEQ_USER_ROLE.nextval from dual", order = ORDER.BEFORE)
    private Long userRoleId;

    private Long userId;

    private Long roleId;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public UserRole setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserRole setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getRoleId() {
        return roleId;
    }

    public UserRole setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId=" + userRoleId +
                "userId=" + userId +
                ", roleId=" + roleId +
                "}";
    }
}
