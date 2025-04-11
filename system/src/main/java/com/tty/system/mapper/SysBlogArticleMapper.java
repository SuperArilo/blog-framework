package com.tty.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.system.entity.SysBlogArticle;
import com.tty.system.vo.SysBlogArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysBlogArticleMapper extends BaseMapper<SysBlogArticle> {
    List<SysBlogArticleVo> aQueryBlogArticle(@Param("aTitle") String articleTitle,
                                             @Param("author") String author,
                                             @Param("beforeTime") String beforeTime,
                                             @Param("afterTime") String afterTime,
                                             @Param("sortName") String sortName,
                                             @Param("sort") String sort);
}
