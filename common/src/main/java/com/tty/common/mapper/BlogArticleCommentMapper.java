package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tty.common.vo.BlogCommentVO;
import com.tty.common.entity.BlogArticleComment;
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
