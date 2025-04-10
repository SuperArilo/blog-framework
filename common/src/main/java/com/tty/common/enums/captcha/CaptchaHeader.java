package com.tty.common.enums.captcha;

import lombok.Getter;

@Getter
public enum CaptchaHeader {

    Code("Authentication-Code"),
    Type("Authentication-Type");
    private final String s;
    CaptchaHeader(String s) {
        this.s = s;
    }

}
