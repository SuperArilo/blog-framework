package com.tty.system.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysUserEntity;
import com.tty.system.service.SysLoginService;


@RestController
@RequestMapping("/sys")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody SysUserEntity user){
        return sysLoginService.login(user);
    }

    @PostMapping("/logout")
    public JsonResult logout(HttpServletRequest request){
        return sysLoginService.logout(request);
    }

}
