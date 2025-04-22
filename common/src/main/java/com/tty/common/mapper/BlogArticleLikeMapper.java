package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogArticleLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogArticleLikeMapper extends BaseMapper<BlogArticleLike> {
    Long queryArticleLikes(@Param("articleId") Long articleId);
}
