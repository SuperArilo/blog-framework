package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum ResourcePath {
    WINDOWS("D:/Temp/files/"),
    LINUX("/www/files/");
    private final String path;
    ResourcePath(String path) {
        this.path = path;
    }
}
