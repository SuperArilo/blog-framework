package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_notice_setting")
public class SysNoticeSetting {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "title")
    private String title;
    @TableField(value = "templateType")
    private String templateType;
    @TableField(value = "settingTemplateId")
    private Long settingTemplateId;
    @TableField(value = "settingClass")
    private Integer settingClass;
}
