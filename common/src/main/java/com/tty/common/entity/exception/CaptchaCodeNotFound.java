package com.tty.common.entity.exception;

import lombok.Getter;

@Getter
public class CaptchaCodeNotFound extends Exception {
    private final String message;
    public CaptchaCodeNotFound(String message) {
        this.message = message;
    }
}
