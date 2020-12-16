package com.drsg.gochat.v1.service.impl;

import com.drsg.gochat.v1.config.OnlineUsers;
import com.drsg.gochat.v1.entity.Room;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.exception.BusinessException;
import com.drsg.gochat.v1.mapper.RoomMapper;
import com.drsg.gochat.v1.mapper.UserInfoMapper;
import com.drsg.gochat.v1.service.RoomService;
import com.drsg.gochat.v1.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implement for RoomService
 *
 * @see RoomService
 * @author YXs
 */

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomMapper roomMapper;
    private final PasswordEncoder passwordEncoder;
    private final OnlineUsers onlineUsers;
    private final UserInfoMapper userInfoMapper;

    public RoomServiceImpl(RoomMapper roomMapper, PasswordEncoder passwordEncoder, OnlineUsers onlineUsers, UserInfoMapper userInfoMapper) {
        this.roomMapper = roomMapper;
        this.passwordEncoder = passwordEncoder;
        this.onlineUsers = onlineUsers;
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public PageSerializable<Room> getRoomList(int pageNum, Room room) {
        PageSerializable<Room> pageSerializable = PageHelper.startPage(pageNum, 10).doSelectPageSerializable(() -> this.roomMapper.selectList(room));
        for (Room roomInfo: pageSerializable.getList()) {
            roomInfo.setOnlineCount(this.onlineUsers.getOnlineUsersCountByRoom(roomInfo.getRoomId()));
        }
        return pageSerializable;
    }

    @Override
    public Room selectByRoomId(Long roomId, String password) {
        Room room = this.roomMapper.selectByRoomId(roomId);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (room != null) {
            // Current user is the owner of the room. No password required.
            if (principal instanceof UserInfo) {
                UserInfo userInfo = (UserInfo) principal;
                if (userInfo.getUserId().equals(room.getUserId())) {
                    room.eraseCredentials();
                }
            }
            // private room.
            else if (room.getIsPrivate()) {
                // 密码匹配成功则擦除密码
                if (passwordEncoder.matches(password, room.getPassword()))  {
                    room.eraseCredentials();
                } else {
                    return null;
                }
            }
            Map<String, Long> onlineUsersByRoom = this.onlineUsers.getOnlineUsersByRoom(room.getRoomId());
            if (onlineUsersByRoom != null && !onlineUsersByRoom.isEmpty()) {
                Set<Long> ids = new HashSet<>(onlineUsersByRoom.values());
                Set<UserInfo> userInfos = this.userInfoMapper.selectByIds(ids);
                room.setOnlineUsers(userInfos);
            }
        }
        return room;
    }

    @Override
    public int insertRoom(Room room) {
        room.setCreateTime(LocalDateTime.now());
        if (room.getIsPrivate()) {
            if (StringUtils.isNotEmpty(room.getPassword())) {
                room.setPassword(passwordEncoder.encode(room.getPassword()));
            } else {
                throw new BusinessException("私密房间必须输入密码");
            }
        } else {
            room.setIsPrivate(false).setPassword(null);
        }
        return this.roomMapper.insertSelective(room);
    }

    @Override
    public Room selectByUserId(Long userId) {
        return this.roomMapper.selectByUserId(userId);
    }
}
