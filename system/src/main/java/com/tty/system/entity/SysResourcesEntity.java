package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_resources")
public class SysResourcesEntity extends BaseEntity {

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 父ids（逗号隔开）
     */
    @TableField("parent_ids")
    private String parentIds;

    /**
     * 业务唯一id
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 资源名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 模块名
     */
    @TableField("module")
    private String module;

    /**
     * 路径
     */
    @TableField("url")
    private String url;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

}
