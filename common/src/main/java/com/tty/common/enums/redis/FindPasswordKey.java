package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum FindPasswordKey {
    Main("FindPassword"),
    Code("code"),
    UUID("uuid");
    private final String key;
    FindPasswordKey(String key) {
        this.key = key;
    }
}
