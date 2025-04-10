package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum RegisterKey {
    Main("register");
    private final String key;
    RegisterKey(String key) {
        this.key = key;
    }
}
