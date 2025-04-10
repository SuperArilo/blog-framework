package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
