package com.tty.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BlogVisitorVO {
    private Long id;
    private Long uid;
    private Date visitTime;
    private String avatar;
    private String nickName;
    private String visitTimeFormat;
}
