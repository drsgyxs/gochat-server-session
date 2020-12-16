package com.drsg.gochat.v1.service.impl;


import com.drsg.gochat.v1.entity.RoleInfo;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.entity.UserRole;
import com.drsg.gochat.v1.exception.BusinessException;
import com.drsg.gochat.v1.exception.RegisterException;
import com.drsg.gochat.v1.mapper.RoleInfoMapper;
import com.drsg.gochat.v1.mapper.UserInfoMapper;
import com.drsg.gochat.v1.mapper.UserRoleMapper;
import com.drsg.gochat.v1.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author YXs
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleInfoMapper roleInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final String passPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9._~!@#$^&*]{8,18}$";

    public UserInfoServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = this.userInfoMapper.loadByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("Username not found.");
        }
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoById(Long userId) {
        UserInfo userInfo = this.userInfoMapper.selectByPrimaryKey(userId);
        userInfo.eraseCredentials();
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void insertUserInfo(UserInfo userInfo) {
        userInfo.setEnabled(true)
                .setUsername(userInfo.getUsername().trim())
                .setPassword(passwordEncoder.encode(userInfo.getPassword()))
                .setCreateTime(LocalDateTime.now())
                .setChannel("GoChat")
                .setNickname(userInfo.getUsername())
                .setEmailVerified(false);
        int usernameCount = userInfoMapper.selectCount(new UserInfo().setUsername(userInfo.getUsername()));
        if (usernameCount > 0) {
            throw new RegisterException("用户名已存在");
        }
        int emailCount = userInfoMapper.selectCount(new UserInfo().setEmail(userInfo.getEmail()));
        if (emailCount > 0) {
            throw new RegisterException("该邮箱已被另一个账号使用");
        }
        userInfoMapper.insertSelective(userInfo);
        RoleInfo roleInfo = roleInfoMapper.selectOne(new RoleInfo().setRoleName("USER"));
        userRoleMapper.insert(new UserRole().setUserId(userInfo.getUserId()).setRoleId(roleInfo.getRoleId()));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateUserInfo(UserInfo userInfo) {
        if (userInfo.getPassword() != null) {
            UserInfo originUserInfo = this.userInfoMapper.selectByPrimaryKey(userInfo.getUserId());
            if (passwordEncoder.matches(userInfo.getPassword(), originUserInfo.getPassword())) {
                if (userInfo.getNewPassword().matches(passPattern)) {
                    if (Objects.equals(userInfo.getNewPassword(), userInfo.getCheckPassword())) {
                        userInfo.setPassword(passwordEncoder.encode(userInfo.getNewPassword()));
                    } else {
                        throw new BusinessException("两次密码不一致");
                    }
                } else {
                    throw new BusinessException("密码不合法");
                }
            } else {
                throw new BusinessException("密码错误");
            }
        }
        return this.userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public PageSerializable<UserInfo> selectList(UserInfo userInfo, Integer pageNum) {
        return PageHelper.startPage(pageNum, 10).doSelectPageSerializable(() -> userInfoMapper.selectList(userInfo));
    }
}
