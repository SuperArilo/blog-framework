package com.tty.common.enums.captcha;

import lombok.Getter;

@Getter
public enum Captcha {
    Main("captcha"),
    Image("image");
    private final String key;
    Captcha(String key) {
        this.key = key;
    }
}
