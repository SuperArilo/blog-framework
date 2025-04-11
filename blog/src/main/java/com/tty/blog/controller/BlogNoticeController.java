package com.tty.blog.controller;

import com.tty.blog.service.BlogNoticeService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class BlogNoticeController {
    @Resource
    BlogNoticeService blogNoticeService;

    @GetMapping("/list")
    public JsonResult noticeList(PageUtil pageUtil,
                                 @RequestParam(value = "classType", defaultValue = "1") Integer classType,
                                 HttpServletRequest request) {
        return blogNoticeService.queryNoticeList(pageUtil, classType, request);
    }
    @PostMapping("/read")
    public JsonResult readNotice(@RequestParam(value = "noticeIds") List<Long> noticeIds,
                                 HttpServletRequest request) {
        return blogNoticeService.userReadNotice(noticeIds, request);
    }
}
