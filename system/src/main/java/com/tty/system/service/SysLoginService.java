package com.tty.system.service;


import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysUserEntity;
import jakarta.servlet.http.HttpServletRequest;


public interface SysLoginService {

    JsonResult login(String email, String password);

    JsonResult logout(HttpServletRequest request);
}
