package com.tty.system.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.tty.system.service.SysLogService;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;
}
