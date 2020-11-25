package com.drsg.gochat.v1.mapper;


import com.drsg.gochat.v1.base.BaseMapper;
import com.drsg.gochat.v1.entity.UserInfo;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfo loadByUsername(String username);
    List<UserInfo> selectList(UserInfo userInfo);
}