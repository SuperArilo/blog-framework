package com.tty.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS_PATTERN, timezone = DateUtil.TIMEZONE_GMT_8)
    private Date createTime;

    /**
     * 创建者
     */
    @TableField
    @JsonIgnore
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS_PATTERN, timezone = DateUtil.TIMEZONE_GMT_8)
    private Date updateTime;

    /**
     * 更新者
     */
    @TableField
    @JsonIgnore
    private Long updateBy;

    /**
     * 删除标志（1，删除，0 未删除 【默认】）
     */
    @TableField("is_deleted")
    @JsonIgnore
    private Boolean deleted = false;
}
