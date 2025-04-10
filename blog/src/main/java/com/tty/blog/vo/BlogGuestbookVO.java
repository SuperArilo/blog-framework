package com.tty.blog.vo;

import lombok.Data;

import java.util.Date;
@Data
public class BlogGuestbookVO {

    private Long guestbookId;
    private Long publisher;
    private Date createTime;
    private String createTimeFormat;
    private String content;
    private String nickName;
    private String avatar;

}
