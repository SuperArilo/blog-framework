package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum SQLTemplateType {

    Avatar("avatar"),
    NickName("nickName"),
    Autograph("autograph"),
    Age("age"),
    Sex("sex"),
    Location("location"),
    Background("background");
    private final String key;

    SQLTemplateType(String key) {
        this.key = key;
    }
}
