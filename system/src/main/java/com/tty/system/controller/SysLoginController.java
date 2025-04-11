package com.tty.system.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysUserEntity;
import com.tty.system.service.SysLoginService;


@RestController
@RequestMapping("/sys")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @PostMapping("/login")
    public JsonResult login(@RequestParam("email") String email, @RequestParam("password") String password){
        return sysLoginService.login(email, password);
    }

    @PostMapping("/logout")
    public JsonResult logout(HttpServletRequest request){
        return sysLoginService.logout(request);
    }

}
