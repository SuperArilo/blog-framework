package com.tty.common.entity.exception;

import lombok.Getter;

@Getter
public class CaptchaHeaderError extends Exception {
    private final String message;
    public CaptchaHeaderError(String message) {
        this.message = message;
    }
}
