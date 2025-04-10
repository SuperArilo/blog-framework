package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum UserKey {
    User("user"),
    Token("token");
    private final String key;

    UserKey(String key) {
        this.key = key;
    }

}
