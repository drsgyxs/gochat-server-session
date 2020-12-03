package com.drsg.gochat.v1.service.impl;

import com.drsg.gochat.v1.entity.Room;
import com.drsg.gochat.v1.exception.BusinessException;
import com.drsg.gochat.v1.mapper.RoomMapper;
import com.drsg.gochat.v1.service.RoomService;
import com.drsg.gochat.v1.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomMapper roomMapper;
    private final PasswordEncoder passwordEncoder;

    public RoomServiceImpl(RoomMapper roomMapper, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.roomMapper = roomMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageSerializable<Room> getRoomList(int pageNum) {
        return PageHelper.startPage(pageNum, 10).doSelectPageSerializable(this.roomMapper::selectList);
    }

    @Override
    public Room getRoomById(Long id, String password) {
        Room room = this.roomMapper.selectByPrimaryKey(id);
        if (room != null) {
            // 私密房间
            if (room.getIsPrivate()) {
                // 密码匹配成功则擦除密码
                if (passwordEncoder.matches(password, room.getPassword()))  {
                    room.eraseCredential();
                    return room;
                }
            }
            // 非私密房间直接返回房间信息
            else
                return room;
        }
        return null;
    }

    @Override
    public int insertRoom(Room room) {
        room.setCreateTime(LocalDateTime.now());
        if (room.getIsPrivate()) {
            if (StringUtils.isNotEmpty(room.getPassword()))
                room.setPassword(passwordEncoder.encode(room.getPassword()));
            else
                throw new BusinessException("私密房间必须输入密码");
        } else
            room.setIsPrivate(false).setPassword(null);
        return this.roomMapper.insertSelective(room);
    }
}
