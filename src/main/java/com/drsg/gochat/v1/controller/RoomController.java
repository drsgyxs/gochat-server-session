package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.entity.Room;
import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.service.RoomService;
import com.github.pagehelper.PageSerializable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author YXs
 */
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<PageSerializable<Room>> getRoomList(@RequestParam(value = "p", required = false, defaultValue = "1") int pageNum,
                                                              Room room) {
        return ResponseEntity.ok(roomService.getRoomList(pageNum, room));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomInfo(@PathVariable("roomId") Long roomId, String password) {
        return ResponseEntity.ok(this.roomService.selectByRoomId(roomId, password));
    }

    @PostMapping
    public ResponseEntity<Integer> createRoom(@RequestBody Room room, Authentication authentication) {
        room.setUserId(((UserInfo) authentication.getPrincipal()).getUserId());
        return ResponseEntity.ok(this.roomService.insertRoom(room));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #userId == authentication.principal.userId)")
    public ResponseEntity<Room> getRoomInfo(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(this.roomService.selectByUserId(userId));
    }

}
