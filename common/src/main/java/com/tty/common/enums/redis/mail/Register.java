package com.tty.common.enums.redis.mail;

import lombok.Getter;

@Getter
public enum Register {
    Main("mail"),
    Register("register"),
    Sent("sent"),
    Code("code");
    private final String key;
    Register(String key) {
        this.key = key;
    }
}
