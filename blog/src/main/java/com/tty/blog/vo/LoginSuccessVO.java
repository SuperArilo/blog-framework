package com.tty.blog.vo;

import lombok.Getter;

@Getter
public class LoginSuccessVO {
    private BlogUserInfoVO user;
    private String token;

    public void setUser(BlogUserInfoVO user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }
}