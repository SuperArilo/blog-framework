package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tty.common.entity.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dictionary")
public class SysDictionaryEntity extends BaseEntity {

    /**
     * 父id,顶级用0表示
     */
    @TableField
    private Long parentId = 0L;

    /**
     * 类型
     */
    @TableField
    private String type;

    /**
     * 名称
     */
    @TableField("item_name")
    private String name;

    /**
     * 值
     */
    @TableField("item_value")
    private String value;

    /**
     * 描述
     */
    @TableField
    private String itemDescribe;

    /**
     * 排序(从1 开始)
     */
    @TableField
    private int sort;

    /**
     * 状态（1 启用 默认， 0 禁用）
     */
    @TableField
    private Boolean status = true;

    /**
     * 是否可编辑的（1 可编辑 默认， 0 不可编辑）
     */
    @TableField("is_editable")
    private Boolean editable = true;
}
