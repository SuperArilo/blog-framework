package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum EncodeType {
    GZIP("gzip"),
    ZSTD("zstd"),
    DEFAULT("default"),
    NONE("none");

    private final String key;

    EncodeType(String key) {
        this.key = key;
    }
}
