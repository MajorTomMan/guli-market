package com.atguigu.gulimall.member.exception;

public class UserNameExistException extends RuntimeException {
    public UserNameExistException() {
        super("用户名不存在");
    }
}
