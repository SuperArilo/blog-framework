package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum ArticleKey {
    Article("article"),
    Ips("ips");
    private final String key;

    ArticleKey(String key) {
        this.key = key;
    }

}
