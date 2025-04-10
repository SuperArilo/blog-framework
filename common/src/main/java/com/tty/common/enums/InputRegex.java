package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum InputRegex {
    Email("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$"),
    NickName("^[\\u4e00-\\u9fa5A-Za-z0-9]{2,7}$"),
    Password("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    private final String regex;

    InputRegex(String regex) {
        this.regex = regex;
    }

}
