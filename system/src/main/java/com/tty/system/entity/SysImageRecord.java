package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_image_record")
public class SysImageRecord {
    @TableId(value = "imageId")
    private Long imageId;
    @TableField(value = "uploader")
    private Long uploader;
    @TableField(value = "imageName")
    private String imageName;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "suffix")
    private String suffix;
    @TableField(value = "path")
    private String path;
    @TableField(value = "type")
    private Integer type;
}
