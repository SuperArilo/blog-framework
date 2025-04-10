package com.tty.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("blog_friends")
public class BlogFriend {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "friendName")
    private String friendName;
    @TableField(value = "friendLocation")
    private String friendLocation;
    @TableField(value = "friendIntroduction")
    private String friendIntroduction;
    @TableField(value = "friendAvatar")
    private String friendAvatar;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "friendUid")
    private Long friendUid;

}
