package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.guestbook.Guestbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class GuestbookConfig implements WebMvcConfigurer {

    private static final String GUESTBOOKPATH = "/api/guestbook";
    @Bean
    public Guestbook guestbook() {
        return new Guestbook();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.guestbook())
                .addPathPatterns(
                        GUESTBOOKPATH + "/add",
                        GUESTBOOKPATH + "/delete"
                )
                .excludePathPatterns(GUESTBOOKPATH + "/list");
    }
}