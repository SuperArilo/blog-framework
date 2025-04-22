package com.tty.common.vo;

import lombok.Data;

@Data
public class LoginSuccessVO {
    private BlogUserInfoVO user;
    private String token;
}