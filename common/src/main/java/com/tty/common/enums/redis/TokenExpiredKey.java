package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum TokenExpiredKey {
    Main("expired");
    private final String key;
    TokenExpiredKey(String key) {
        this.key = key;
    }
}
