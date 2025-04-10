package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {

    /**
     * 用户名称
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 用户头像
     */
    @TableField("head_portrait")
    private String headPortrait;

    /**
     * 用户类型(1 超级管理员、2 管理员、3 普通用户【 默认】)
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 用户手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;
}
