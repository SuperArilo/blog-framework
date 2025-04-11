package com.tty.blog.controller;

import com.tty.blog.service.BlogArticleService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class BlogArticleController {
    @Resource
    BlogArticleService blogArticleService;
    @GetMapping("/list")
    public JsonResult blogArticleListGet(PageUtil pageUtil,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         HttpServletRequest request){
        return this.blogArticleService.getBlogArticleList(pageUtil, keyword, request);
    }
    @GetMapping("/content")
    public JsonResult blogArticleContentGet(@RequestParam(value = "articleId") Long articleId,
                                            HttpServletRequest request){
        return this.blogArticleService.getBlogArticleContent(articleId, request);
    }
    @PutMapping("/like")
    public JsonResult increaseBlogArticleLike(@RequestParam(value = "articleId") Long articleId,
                                              HttpServletRequest request){
        return this.blogArticleService.increaseBlogArticleLike(articleId, request);
    }
}
