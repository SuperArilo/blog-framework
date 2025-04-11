package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.friend.Friend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class FriendConfig implements WebMvcConfigurer {
    private static final String FRIENDPATH = "/api/friend";
    @Bean
    public Friend friend() {
        return new Friend();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.friend())
                .addPathPatterns(FRIENDPATH + "/apply")
                .excludePathPatterns(FRIENDPATH + "/list");
    }
}
