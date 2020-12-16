package com.drsg.gochat.v1.service;

import com.drsg.gochat.v1.entity.Room;
import com.github.pagehelper.PageSerializable;

/**
 * Core interface which loads room information.
 *
 * @author YXs
 */
public interface RoomService {
    /**
     * 根据页码获取房间列表
     *
     * @param pageNum 页码
     * @return 分页信息
     */
    PageSerializable<Room> getRoomList(int pageNum, Room room);

    /**
     * 根据ID获取房间信息，如果为私密房间则验证密码
     *
     * @param roomId 房间id
     * @param password 房间密码（可选）
     * @return 房间信息
     */
    Room selectByRoomId(Long roomId, String password);

    /**
     * 创建房间
     *
     * @param room 房间信息
     * @return 受影响行数
     */
    int insertRoom(Room room);

    Room selectByUserId(Long userId);
}
