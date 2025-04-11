package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.service.SysBlogArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/article")
public class SysBlogArticleController {

    @Resource
    private SysBlogArticleService blogArticleService;

    @GetMapping("/list")
    public JsonResult ABlogArticleGet(@RequestParam(value = "articleTitle", required = false) String articleTitle,
                                      @RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "beforeTime", required = false) String beforeTime,
                                      @RequestParam(value = "afterTime", required = false) String afterTime,
                                      @RequestParam(value = "sortName", required = false) String sortName,
                                      @RequestParam(value = "sort", required = false , defaultValue = "id") String sort,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        return this.blogArticleService.queryBlogArticle(pageNum, pageSize, articleTitle, author, beforeTime, afterTime, sortName, sort);
    }
}
