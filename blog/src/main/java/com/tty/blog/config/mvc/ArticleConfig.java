package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.article.Article;
import com.tty.blog.interceptor.article.ArticleComment;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ArticleConfig implements WebMvcConfigurer {

    private static final String ARTICLEPATH = "/article";
    private static final String ARTICLECOMMENTPATH = ARTICLEPATH + "/comment";

    @Bean
    public Article article() {
        return new Article();
    }

    @Bean
    public ArticleComment articleComment() {
        return new ArticleComment();
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(this.article())
                .addPathPatterns(ARTICLEPATH + "/like")
                .excludePathPatterns(
                        ARTICLEPATH + "/list",
                        ARTICLEPATH + "/content"
                );
        registry.addInterceptor(this.articleComment())
                .addPathPatterns(
                        ARTICLECOMMENTPATH + "/add",
                        ARTICLECOMMENTPATH + "/delete",
                        ARTICLECOMMENTPATH + "/like"
                )
                .excludePathPatterns(ARTICLECOMMENTPATH + "/list");
    }
}
