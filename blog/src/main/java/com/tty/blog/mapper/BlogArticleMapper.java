package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogArticle;
import com.tty.blog.vo.article.ArticleContentVO;
import com.tty.blog.vo.article.BlogArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {
    List<BlogArticleVO> selectBlogArticleList(@Param("keywords") String keywords);
    ArticleContentVO queryArticle(@Param("articleId") Long articleId,
                                  @Param("uid") Long uid);
    boolean increaseArticleView(@Param("articleId") Long articleId);
}
