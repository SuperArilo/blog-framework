package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName(value = "sys_notice")
public class SysNotice {
    @TableId(value = "id", type = IdType.AUTO)
    private Long noticeId;
    @TableField(value = "title")
    private String title;
    @TableField(value = "content")
    private String content;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "classType")
    private Integer classType;
    @TableField(value = "authorUid")
    private Long authorUid;
    @TableField(value = "targetUid")
    private Long targetUid;
}
