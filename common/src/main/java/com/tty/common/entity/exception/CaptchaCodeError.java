package com.tty.common.entity.exception;

import lombok.Getter;

@Getter
public class CaptchaCodeError extends Exception {
    private final String message;

    public CaptchaCodeError(String message) {
        this.message = message;
    }

}
