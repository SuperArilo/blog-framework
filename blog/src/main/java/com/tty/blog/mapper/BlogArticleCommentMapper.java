package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tty.blog.entity.BlogArticleComment;
import com.tty.blog.vo.BlogCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogArticleCommentMapper extends BaseMapper<BlogArticleComment> {
    Page<BlogCommentVO> getArticleCommentList(Page<BlogArticleComment> page,
                                              @Param("articleId") Long articleId);
    boolean deleteArticleCommentById(@Param("articleId") Long articleId,
                                     @Param("commentId") Long commentId,
                                     @Param("userId") Long replyUser);
}
