package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.entity.SysUserEntity;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.tty.system.service.SysUserService;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 获取用户列表的信息。
     *
     * @param pageUtil 分页参数工具类，包含分页信息和查询条件。
     * @return JsonResult 包含查询结果和操作状态的JSON结果对象。
     */
    @GetMapping("/list")
    private JsonResult getUserList(PageUtil pageUtil) {
        // 调用服务层方法，获取用户列表
        return sysUserService.getUserList(pageUtil);
    }

    @PostMapping("/create")
    public JsonResult create(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("type") Integer type,
            @RequestParam("email") String email) {
        SysUserEntity entity = new SysUserEntity();
        entity.setType(type);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setEmail(email);
        this.sysUserService.createUser(entity);
        return JsonResult.OK("ok");
    }
}
