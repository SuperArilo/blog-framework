package com.tty.blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BlogUserProfilesModifyDTO {
    private String autograph;
    private String nickName;
    private Integer age;
    private Integer sex;
    private MultipartFile avatar;
    private String contact;
//    private MultipartFile background;
    private String password;
}
