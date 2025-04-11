package com.tty.blog.controller;

import com.tty.blog.service.BlogGossipService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gossip")
public class BlogGossipController {
    @Resource
    BlogGossipService blogGossipService;

    @GetMapping("/list")
    public JsonResult gossipListGet(PageUtil pageUtil,
                                    @RequestParam(value = "viewUid", required = false) Long viewUid,
                                    @RequestParam(value = "targetId", required = false) Long targetId,
                                    @RequestParam(value = "commentId", required = false) Long commentId,
                                    HttpServletRequest request){
        return JsonResult.OK("请求成功", this.blogGossipService.gossipListGet(pageUtil, viewUid, targetId, commentId, request));
    }
    @PostMapping("/add")
    public JsonResult gossipAddByUser(@RequestParam(value = "content") String content,
                                      HttpServletRequest request){
        return blogGossipService.gossipCreateByUser(content, request);
    }
    @PutMapping("/like")
    public JsonResult gossipLike(@RequestParam("gossipId") Long gossipId,
                                 HttpServletRequest request){
        return blogGossipService.gossipLike(gossipId, request);
    }
    @DeleteMapping("/delete")
    public JsonResult gossipDelete(@RequestParam("gossipId") Long gossipId,
                                   HttpServletRequest request) {
        return blogGossipService.gossipDeleteByUser(gossipId, request);
    }
}
