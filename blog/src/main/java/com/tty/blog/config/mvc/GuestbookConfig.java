package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.guestbook.Guestbook;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class GuestbookConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public Guestbook guestbook() {
        return new Guestbook();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.guestbook())
                .addPathPatterns(
                        this.basePathProperties.basePath + "/guestbook/add",
                        this.basePathProperties.basePath + "/guestbook/delete"
                )
                .excludePathPatterns(this.basePathProperties.basePath + "/guestbook/list");
    }
}