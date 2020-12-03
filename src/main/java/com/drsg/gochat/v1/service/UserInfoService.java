package com.drsg.gochat.v1.service;

import com.drsg.gochat.v1.entity.UserInfo;
import com.github.pagehelper.PageSerializable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserInfoService extends UserDetailsService {
    UserInfo getUserInfoById(Long userId);
    void insertUserInfo(UserInfo userInfo);
    int updateUserInfo(UserInfo userInfo);

    PageSerializable<UserInfo> selectList(UserInfo userInfo, Integer pageNum);
}
