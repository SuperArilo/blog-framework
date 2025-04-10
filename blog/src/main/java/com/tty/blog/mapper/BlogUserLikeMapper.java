package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogUserLike;
import com.tty.blog.vo.BlogUserLikeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogUserLikeMapper extends BaseMapper<BlogUserLike> {
    List<BlogUserLikeVO> queryLikeList(@Param("targetUid") Long targetUid);
}
