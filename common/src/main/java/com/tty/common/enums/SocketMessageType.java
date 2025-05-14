package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum SocketMessageType {
    WEB("WEB"),
    WEB_OTHER("WEB_OTHER"),
    MINECRAFT("MINECRAFT");

    private final String type;

    SocketMessageType(String type) {
        this.type = type;
    }
}
