package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum SocketMessageType {
    WEB("web"),
    MINECRAFT("minecraft");

    private final String type;

    SocketMessageType(String type) {
        this.type = type;
    }
}
