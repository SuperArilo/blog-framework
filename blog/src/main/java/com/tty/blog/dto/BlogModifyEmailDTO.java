package com.tty.blog.dto;

import lombok.Data;

@Data
public class BlogModifyEmailDTO {

    private String previousToken;
    private String verifyCode;

}
