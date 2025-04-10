package com.tty.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("blog_user_like_list")
public class BlogUserLike {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "uid")
    private Long uid;
    @TableField(value = "targetUid")
    private Long targetUid;
    @TableField(value = "createTime")
    private Date createTime;
}
