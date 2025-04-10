package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.system.utils.BunnyUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/cdn/path")
public class BunnyCDNController {

    @Resource
    BunnyUtil bunnyUtil;

    @GetMapping("/**")
    public JsonResult get(HttpServletRequest request) {
        return JsonResult.OK("ok", this.bunnyUtil.ListFiles(request.getServletPath().replace("/system/cdn/path", "")));
    }

}
