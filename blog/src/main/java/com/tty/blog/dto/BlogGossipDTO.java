package com.tty.blog.dto;

import lombok.Data;

@Data
public class BlogGossipDTO {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long viewUid;
    private Long targetId;
    private Long commentId;

}
