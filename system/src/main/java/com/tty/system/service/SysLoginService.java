package com.tty.system.service;


import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysUserEntity;
import jakarta.servlet.http.HttpServletRequest;


public interface SysLoginService {

    /**
     * 登录
     * @param user 用户信息
     * @return JsonResult
     */
    JsonResult login(SysUserEntity user);

    /**
     * 登出
     * @param request 请求
     * @return JsonResult
     */
    JsonResult logout(HttpServletRequest request);
}
