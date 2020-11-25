package com.drsg.gochat.v1.entity;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = -3288329603098632585L;
    private int code;
    private String message;
    private T data;
    public static int OK = 200;
    public static int FAIL = 500;
    public static int UNAUTHORIZED = 403;

    public static Result<?> ok() {
        return new Result<>();
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(data);
    }

    public static Result<?> fail(String message) {
        return new Result<>(message);
    }

    public static Result<?> fail(int code, String message) {
        return new Result<>(code, message);
    }

    public Result() {
        this.code = OK;
        this.message = "OK";
    }

    public Result(T data) {
        this.code = OK;
        this.message = "OK";
        this.data = data;
    }

    public Result(String message) {
        this.code = FAIL;
        this.message = message;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
