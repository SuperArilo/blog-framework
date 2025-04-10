package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum ImageType {
    Normal(1),
    Emoji(2),
    MaterialWeb(3);
    private final Integer type;
    ImageType(Integer type) {
        this.type = type;
    }
}
