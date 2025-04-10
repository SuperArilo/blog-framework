package com.tty.blog.vo;

import lombok.Data;

@Data
public class BlogUserInfoVO {

    private Long uid;
    private String email;
    private String registerTime;
    private String avatar;
    private String nickName;
    private Integer age;
    private Integer sex;
    private String autograph;
    private String background;

}
