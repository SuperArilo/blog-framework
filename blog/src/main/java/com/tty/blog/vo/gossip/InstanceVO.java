package com.tty.blog.vo.gossip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class InstanceVO {
    private Long id;
    private String content;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    private Date createTime;
    private String createTimeFormat;
    private Long author;
    private String categoryName;
    private String avatar;
    private String nickName;
    private Integer comments;
    private Integer likes;
    private Boolean like;
}
