package com.tty.blog.controller;

import com.tty.blog.service.BlogUserLikeService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/like")
public class BlogUserLikeController {
    @Resource
    BlogUserLikeService blogUserLikeService;
    @GetMapping("/list")
    public JsonResult listGet(PageUtil pageUtil,
                              @RequestParam(value = "targetUid") Long targetUid) {
        return this.blogUserLikeService.likeListGet(pageUtil, targetUid);
    }
    @PostMapping("/apply")
    public JsonResult likeUser(@RequestParam(value = "targetUid") Long targetUid,
                               HttpServletRequest request) {
        return this.blogUserLikeService.likeTarget(targetUid, request);
    }
}
