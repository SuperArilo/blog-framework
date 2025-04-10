package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogArticleComment;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface BlogArticleCommentService extends IService<BlogArticleComment> {
    JsonResult articleCommentListGet(PageUtil pageUtil, Long articleId, HttpServletRequest request);
    JsonResult articleCommentAdd(Long articleId, String content, Long replyCommentId, Long replyUserId, HttpServletRequest request);
    JsonResult deleteArticleCommentById(Long articleId, Long commentId, HttpServletRequest request, HttpServletResponse response);
    JsonResult commentLike(Long commentId, Long articleId, HttpServletRequest request);
}
