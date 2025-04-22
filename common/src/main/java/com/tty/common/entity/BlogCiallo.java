package com.tty.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@TableName(value = "blog_ciallo")
@Data
public class BlogCiallo {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "title")
    private String title;
    @TableField(value = "imageUrl")
    private String imageUrl;
    @TableField(value = "audioUrl")
    private String audioUrl;
    @TableField(value = "author")
    private Long author;
    @TableField(value = "createTime")
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_BIAS_PATTERN)
    private Date createTime;
}
