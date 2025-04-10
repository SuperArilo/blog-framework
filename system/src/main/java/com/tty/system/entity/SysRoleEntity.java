package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {

    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

}
