package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.friend.Friend;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class FriendConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;
    @Bean
    public Friend friend() {
        return new Friend();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.friend())
                .addPathPatterns(this.basePathProperties.basePath + "/friend/apply")
                .excludePathPatterns(this.basePathProperties.basePath + "/friend/list");
    }
}
