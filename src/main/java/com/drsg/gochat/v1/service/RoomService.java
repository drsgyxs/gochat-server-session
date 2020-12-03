package com.drsg.gochat.v1.service;

import com.drsg.gochat.v1.entity.Room;
import com.github.pagehelper.PageSerializable;

import java.security.Principal;

public interface RoomService {
    PageSerializable<Room> getRoomList(int pageNum);
    Room getRoomById(Long id, String password);
    int insertRoom(Room room);
}
