package com.tty.blog.controller;

import com.tty.blog.service.BlogVisitorService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitor")
public class BlogVisitorController {
    @Resource
    BlogVisitorService blogVisitorService;
    @GetMapping("/list")
    public JsonResult visitorList(PageUtil pageUtil) {
        return blogVisitorService.visitorList(pageUtil);
    }
}
