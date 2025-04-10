package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.system.service.SysCialloService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sys/ciallo")
public class SysCialloController {
    @Resource
    SysCialloService sysCialloService;
    @PostMapping("/add")
    public JsonResult cialloAdd(@RequestParam("title") String title,
                                @RequestParam("image") MultipartFile imageFile,
                                @RequestParam("audio") MultipartFile audioFile,
                                HttpServletRequest request) {
        return sysCialloService.cialloAdd(title, imageFile, audioFile, request);
    }
}
