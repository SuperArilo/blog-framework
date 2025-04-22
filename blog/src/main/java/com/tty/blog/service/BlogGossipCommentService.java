package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.entity.BlogGossipComment;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogGossipCommentService extends IService<BlogGossipComment> {
    JsonResult commentList(PageUtil pageUtil,
                           Long gossipId,
                           HttpServletRequest request);
    JsonResult createComment(Long gossipId,
                             String content,
                             Long replyCommentId,
                             Long replyUserId,
                             HttpServletRequest request);
    JsonResult commentLike(Long gossipId,
                           Long commentId,
                           HttpServletRequest request);
    JsonResult deleteGossipCommentById(Long gossipId,
                                       Long commentId,
                                       HttpServletRequest request);

}
