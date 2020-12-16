package com.drsg.gochat.v1.mapper;

import com.drsg.gochat.v1.base.BaseMapper;
import com.drsg.gochat.v1.entity.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YXs
 */
@Repository
public interface RoomMapper extends BaseMapper<Room> {
    List<Room> selectList(Room room);
    Room selectByRoomId(Long roomId);
    Room selectByUserId(Long userId);
}
