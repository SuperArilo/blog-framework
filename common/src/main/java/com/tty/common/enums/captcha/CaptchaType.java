package com.tty.common.enums.captcha;

import lombok.Getter;

@Getter
public enum CaptchaType {
    LOGIN("login"),
    LOGINBYTOKEN("loginByToken"),
    REGISTER("register");
    private final String type;
    CaptchaType(String type) {
        this.type = type;
    }
}
