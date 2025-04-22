package com.tty.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
@TableName("blog_guestbook")
public class BlogGuestbook {
    @TableId(value = "guestbookId", type = IdType.AUTO)
    private Long guestbookId;
    @TableField(value = "publisher")
    private Long publisher;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "content")
    private String content;
}
