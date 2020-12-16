package com.drsg.gochat.v1.mapper;


import com.drsg.gochat.v1.base.BaseMapper;
import com.drsg.gochat.v1.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author YXs
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 存在则返回用户信息，否则为空
     */
    UserInfo loadByUsername(String username);

    /**
     * 条件查询
     * @param userInfo 查询条件
     * @return 用户信息集合
     */
    List<UserInfo> selectList(UserInfo userInfo);

    /**
     * 根据ID集合查询
     * @param ids ID集合
     * @return 用户集合
     */
    Set<UserInfo> selectByIds(Collection<Long> ids);
}