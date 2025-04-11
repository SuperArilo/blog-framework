package com.tty.blog.controller;

import com.tty.blog.service.CaptchaService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Resource
    CaptchaService captchaService;

    @GetMapping("/image")
    public void identifyImage(@RequestParam(value = "type") String type,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        captchaService.imageProcess(type, request, response);
    }

    @GetMapping("/base64")
    public String identifyBase64(@RequestParam(value = "codeId") String codeId, HttpServletRequest request, HttpServletResponse response) {
        return captchaService.base64Process(codeId, request, response);
    }

}
