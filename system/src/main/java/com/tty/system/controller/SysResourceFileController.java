package com.tty.system.controller;

import com.tty.system.service.SysManagerFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class SysResourceFileController {
    @Resource
    SysManagerFileService managerFileService;
    @GetMapping("/**")
    public ResponseEntity<Object> resourceFile(HttpServletRequest request) {
        return managerFileService.resourceFile(request);
    }
}
