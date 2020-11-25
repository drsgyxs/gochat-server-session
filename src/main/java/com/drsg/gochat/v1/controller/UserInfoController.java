package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.service.UserInfoService;
import com.drsg.gochat.v1.utils.ImageUploadUtils;
import com.github.pagehelper.PageSerializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @author YXs
 * @since 2020-11-12
 */
@RestController
@RequestMapping("/v1/users")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final ImageUploadUtils imageUploadUtils;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, ImageUploadUtils imageUploadUtils) {
        this.userInfoService = userInfoService;
        this.imageUploadUtils = imageUploadUtils;
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<Integer> uploadAvatar(@RequestParam("file") MultipartFile image, @PathVariable("userId") Long userId) throws IOException {
        String imageUrl = imageUploadUtils.storeImage(image);
        UserInfo userInfo = new UserInfo().setUserId(userId).setAvatarUrl(imageUrl);
        return ResponseEntity.ok(this.userInfoService.updateUserInfo(userInfo));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getMyInfo() {
        return ResponseEntity.ok(this.userInfoService.getMyInfo());
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<Integer> updateUserInfo(@RequestBody UserInfo userInfo, @PathVariable("userId") Long userId) {
        userInfo.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userInfoService.updateUserInfo(userInfo));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageSerializable<UserInfo>> selectList(UserInfo userInfo, int p) {
        return ResponseEntity.ok(this.userInfoService.selectList(userInfo, p));
    }
}

