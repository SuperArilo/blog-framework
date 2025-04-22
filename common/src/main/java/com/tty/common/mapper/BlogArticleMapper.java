package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogArticle;
import com.tty.common.vo.article.ArticleContentVO;
import com.tty.common.vo.article.BlogArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {
    List<BlogArticleVO> selectBlogArticleList(@Param("keywords") String keywords);
    ArticleContentVO queryArticle(@Param("articleId") Long articleId,
                                  @Param("uid") Long uid);
    boolean increaseArticleView(@Param("articleId") Long articleId);
    List<BlogArticleVO> aQueryBlogArticle(@Param("aTitle") String articleTitle,
                                             @Param("author") String author,
                                             @Param("beforeTime") String beforeTime,
                                             @Param("afterTime") String afterTime,
                                             @Param("sortName") String sortName,
                                             @Param("sort") String sort);
}
