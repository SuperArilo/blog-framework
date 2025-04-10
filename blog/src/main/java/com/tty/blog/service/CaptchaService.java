package com.tty.blog.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CaptchaService {
    void imageProcess(String type, HttpServletRequest request, HttpServletResponse response);
    String base64Process(String uuid, HttpServletRequest request, HttpServletResponse response);
}
