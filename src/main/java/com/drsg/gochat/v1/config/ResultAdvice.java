package com.drsg.gochat.v1.config;

import com.drsg.gochat.v1.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ResultAdvice {
    private final Logger logger = LoggerFactory.getLogger(ResultAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> businessExceptionHandle(BusinessException e) {
        logger.error(e.getMessage());
        Map<String, Object> map = new HashMap<>(1);
        map.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> exceptionHandle(AccessDeniedException e) {
        logger.error(e.getMessage());
        Map<String, Object> map = new HashMap<>(1);
        map.put("error", "请求被拒绝");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandle(Exception e) {
        logger.error(e.getMessage(), e);
        Map<String, Object> map = new HashMap<>(1);
        map.put("error", "服务器错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }

}
