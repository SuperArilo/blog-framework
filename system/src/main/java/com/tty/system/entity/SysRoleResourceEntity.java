package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 角色资源关系表
 * </p>
 *
 * @author rgc
 * @since 2023-05-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_resource")
public class SysRoleResourceEntity extends BaseEntity {

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 用户id
     */
    @TableField("business_id")
    private String businessId;

}
