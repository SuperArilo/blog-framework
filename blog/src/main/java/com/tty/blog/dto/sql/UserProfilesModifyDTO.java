package com.tty.blog.dto.sql;

import lombok.Data;

@Data
public class UserProfilesModifyDTO {
    private Long uploader;
    private String autograph;
    private String nickName;
    private Integer age;
    private Integer sex;
    private String location;
    private String avatarUrl;
    private String contact;
    private String backgroundUrl;
    private String password;
}
