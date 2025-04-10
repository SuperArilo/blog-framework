package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRoleEntity extends BaseEntity {

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

}
