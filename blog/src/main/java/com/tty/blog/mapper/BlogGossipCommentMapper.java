package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogGossipComment;
import com.tty.blog.vo.BlogCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogGossipCommentMapper extends BaseMapper<BlogGossipComment> {

    List<BlogCommentVO> commentList(@Param("gossipId") Long gossipId);
    BlogCommentVO queryCommentByGossipIdAndCommentId(@Param("gossipId") Long gossipId,
                                                     @Param("commentId") Long commentId);
    boolean deleteCommentById(@Param("gossipId") Long gossipId,
                              @Param("commentId") Long commentId,
                              @Param("uid") Long uid);
}
