package com.tty.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("blog_visitor")
public class BlogVisitor {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "uid")
    private Long uid;
    @TableField(value = "visitTime")
    private Date visitTime;
}
