package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.entity.SysUserEntity;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息对象
     */
    SysUserEntity getUserByUsername(String username);

    /**
     * 获取用户列表的信息。
     *
     * @param pageUtil 分页参数工具类，包含分页信息和查询条件。
     * @return JsonResult 包含查询结果和操作状态的JSON结果对象。
     */
    JsonResult getUserList(PageUtil pageUtil);


    void createUser(SysUserEntity entity);
}
