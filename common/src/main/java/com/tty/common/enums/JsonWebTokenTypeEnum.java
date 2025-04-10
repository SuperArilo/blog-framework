package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum JsonWebTokenTypeEnum {

    SYSTEM("system"),
    USER("user");

    private final String type;

    JsonWebTokenTypeEnum(String type) {
        this.type = type;
    }

}
