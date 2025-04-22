package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogUserLike;
import com.tty.common.vo.BlogUserLikeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogUserLikeMapper extends BaseMapper<BlogUserLike> {
    List<BlogUserLikeVO> queryLikeList(@Param("targetUid") Long targetUid);
}
