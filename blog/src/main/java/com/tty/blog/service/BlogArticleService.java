package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogArticle;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogArticleService extends IService<BlogArticle> {
    JsonResult getBlogArticleList(PageUtil pageUtil, String keyword, HttpServletRequest request);
    JsonResult getBlogArticleContent(Long articleId, HttpServletRequest request);
    JsonResult increaseBlogArticleLike(Long articleId,HttpServletRequest request);
}
