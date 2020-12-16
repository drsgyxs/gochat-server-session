package com.drsg.gochat.v1.entity;

import com.drsg.gochat.v1.base.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author YXs
 */
public class RoleInfo extends BaseEntity implements GrantedAuthority {
    @Id
    @KeySql(sql = "select SEQ_ROLE_INFO.nextval from dual", order = ORDER.BEFORE)
    private Long roleId;

    private String roleName;

    private String description;


    public Long getRoleId() {
        return roleId;
    }

    public RoleInfo setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleInfo setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleInfo roleInfo = (RoleInfo) o;
        return Objects.equals(roleId, roleInfo.roleId) &&
                Objects.equals(roleName, roleInfo.roleName) &&
                Objects.equals(description, roleInfo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName, description);
    }
}
