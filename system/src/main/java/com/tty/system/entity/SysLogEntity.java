package com.tty.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.tty.common.entity.BaseEntity;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_log")
public class SysLogEntity extends BaseEntity {

    /**
     * 用户名称
     */
    @TableField("username")
    private String username;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 执行操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 请求地址
     */
    @TableField("request_url")
    private String requestUrl;

    /**
     * 请求方法
     */
    @TableField("method")
    private String method;

    /**
     * 请求参数
     */
    @TableField("params")
    private String params;

    /**
     * 执行时间(毫秒为单位)
     */
    @TableField("execute_time")
    private Long executeTime;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 所属模块
     */
    @TableField("module")
    private String module;

}
