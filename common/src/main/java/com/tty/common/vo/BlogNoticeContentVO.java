package com.tty.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BlogNoticeContentVO {
    private Long noticeId;
    private String title;
    private String content;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_HH_MM_SS_PATTERN)
    private Date createTime;
    private Integer classType;
    private String classTypeName;
    private String author;
}
