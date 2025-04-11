package com.tty.blog.controller;

import com.tty.blog.service.BlogGossipCommentService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gossip/comment")
public class BlogGossipCommentController {

    @Resource
    BlogGossipCommentService blogGossipCommentService;

    @GetMapping("/list")
    public JsonResult commentListGet(PageUtil pageUtil,
                                     @RequestParam(value = "gossipId") Long gossipId,
                                     HttpServletRequest request){
        return this.blogGossipCommentService.commentList(pageUtil, gossipId, request);
    }
    @PostMapping("/add")
    public JsonResult createComment(@RequestParam(value = "gossipId") Long gossipId,
                                    @RequestParam(value = "content") String content,
                                    @RequestParam(value = "replyCommentId", required = false) Long replyCommentId,
                                    @RequestParam(value = "replyUserId", required = false) Long replyUserId,
                                    HttpServletRequest request){
        return this.blogGossipCommentService.createComment(gossipId, content, replyCommentId, replyUserId, request);
    }
    @DeleteMapping("/delete")
    public JsonResult gossipCommentDelete(@RequestParam("gossipId") Long gossipId,
                                          @RequestParam("commentId") Long commentId,
                                          HttpServletRequest request) {
        return this.blogGossipCommentService.deleteGossipCommentById(gossipId, commentId, request);
    }
    @PutMapping("/like")
    public JsonResult gossipCommentLike(@RequestParam("gossipId") Long gossipId,
                                        @RequestParam("commentId") Long commentId,
                                        HttpServletRequest request) {
        return this.blogGossipCommentService.commentLike(gossipId, commentId, request);
    }
}
