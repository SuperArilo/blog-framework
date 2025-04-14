package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.article.Article;
import com.tty.blog.interceptor.article.ArticleComment;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ArticleConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

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
                .addPathPatterns(this.basePathProperties.basePath + "/article/like")
                .excludePathPatterns(
                        this.basePathProperties.basePath + "/article/list",
                        this.basePathProperties.basePath + "/article/content"
                );
        registry.addInterceptor(this.articleComment())
                .addPathPatterns(
                        this.basePathProperties.basePath + "/article/comment/add",
                        this.basePathProperties.basePath + "/article/comment/delete",
                        this.basePathProperties.basePath + "/article/comment/like"
                )
                .excludePathPatterns(this.basePathProperties.basePath + "/article/comment/list");
    }
}
