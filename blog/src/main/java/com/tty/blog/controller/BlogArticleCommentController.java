package com.tty.blog.controller;

import com.tty.blog.service.BlogArticleCommentService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article/comment")
public class BlogArticleCommentController {

    @Resource
    BlogArticleCommentService blogArticleCommentService;

    @GetMapping("/list")
    public JsonResult articleCommentListGet(PageUtil pageUtil,
                                            @RequestParam(value = "articleId") Long articleId,
                                            HttpServletRequest request){
        return this.blogArticleCommentService.articleCommentListGet(pageUtil, articleId, request);
    }
    @PostMapping("/add")
    public JsonResult articleCommentAdd(@RequestParam(value = "articleId") Long articleId,
                                        @RequestParam(value = "content") String content,
                                        @RequestParam(value = "replyCommentId", required = false) Long replyCommentId,
                                        @RequestParam(value = "replyUserId", required = false) Long replyUserId,
                                        HttpServletRequest request){
        return this.blogArticleCommentService.articleCommentAdd(articleId, content, replyCommentId, replyUserId, request);
    }
    @DeleteMapping("/delete")
    public JsonResult articleCommentDelete(@RequestParam(value = "articleId") Long articleId,
                                           @RequestParam(value = "commentId") Long commentId,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        return this.blogArticleCommentService.deleteArticleCommentById(articleId, commentId, request, response);
    }
    @PutMapping("/like")
    public JsonResult articleCommentLike(@RequestParam(value = "articleId") Long articleId,
                                         @RequestParam(value = "commentId") Long commentId,
                                         HttpServletRequest request){
        return this.blogArticleCommentService.commentLike(commentId, articleId, request);
    }
}
