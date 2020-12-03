package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.service.UserInfoService;
import com.drsg.gochat.v1.utils.ImageUploadUtils;
import com.github.pagehelper.PageSerializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> uploadAvatar(@RequestParam("file") MultipartFile image, @PathVariable("userId") Long userId) throws IOException {
        String imageUrl = imageUploadUtils.storeImage(image);
        UserInfo userInfo = new UserInfo().setUserId(userId).setAvatarUrl(imageUrl);
        return ResponseEntity.ok(this.userInfoService.updateUserInfo(userInfo));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfo> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok(this.userInfoService.getUserInfoById(((UserInfo) authentication.getPrincipal()).getUserId()));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> updateUserInfo(@RequestBody UserInfo userInfo, @PathVariable("userId") Long userId) {
        userInfo.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userInfoService.updateUserInfo(userInfo));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageSerializable<UserInfo>> selectList(UserInfo userInfo,
                                                                 @RequestParam(value = "p", required = false, defaultValue = "1") int pageNum) {
        return ResponseEntity.ok(this.userInfoService.selectList(userInfo, pageNum));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserInfo userInfo) {
        this.userInfoService.insertUserInfo(userInfo);
        Map<String, Object> map = new HashMap<>(1);
        map.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }
}

