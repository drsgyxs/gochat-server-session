package com.drsg.gochat.v1.controller;

import com.drsg.gochat.v1.entity.UserInfo;
import com.drsg.gochat.v1.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class AuthController {
    private final UserInfoService userInfoService;

    @Autowired
    public AuthController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserInfo userInfo) {
        this.userInfoService.insertUserInfo(userInfo);
        Map<String, Object> map = new HashMap<>(1);
        map.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }
}
