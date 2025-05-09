package com.tty.blog.controller;

import com.tty.blog.service.BlogUploadFileService;
import com.tty.common.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/upload")
public class BlogUploadFileController {

    @Resource
    BlogUploadFileService blogUploadFileService;

    @PostMapping("/image")
    public JsonResult uploadImage(@RequestParam(value = "file") MultipartFile file,
                                  HttpServletRequest request){
        if (file.isEmpty()) return JsonResult.OK("上传的数据为空");
        return blogUploadFileService.uploadFile(file, request);
    }
}