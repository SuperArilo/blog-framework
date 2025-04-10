package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.notice.Notice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class NoticeConfig implements WebMvcConfigurer {
    private static final String NOTICEPATH = "/api/notice";
    @Bean
    public Notice notice() {
        return new Notice();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.notice())
                .addPathPatterns(NOTICEPATH + "/**");
    }
}
