package com.tty.blog.controller;

import com.tty.blog.service.BlogCialloService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ciallo")
public class BlogCialloController {
    @Resource
    BlogCialloService blogCialloService;
    @GetMapping("/list")
    public JsonResult cialloList(PageUtil pageUtil) {
        return blogCialloService.serviceList(pageUtil);
    }
    @PostMapping("/add")
    public JsonResult cialloAdd(@RequestParam("title") String title,
                                @RequestParam("image") MultipartFile imageFile,
                                @RequestParam("audio") MultipartFile audioFile,
                                HttpServletRequest request) {
        return blogCialloService.cialloAdd(title, imageFile, audioFile, request);
    }
}
