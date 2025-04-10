package com.tty.common.entity;

import com.tty.common.enums.captcha.CaptchaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CaptchaDto {
    private String code;
    private UUID captchaUUID;
    private CaptchaType captchaType;

    public void setCode(String code) {
        this.code = code;
    }

    public void setCaptchaUUID(UUID captchaUUID) {
        this.captchaUUID = captchaUUID;
    }


    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }
}
