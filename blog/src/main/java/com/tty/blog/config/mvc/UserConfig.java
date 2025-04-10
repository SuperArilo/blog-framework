package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.user.TokenLogin;
import com.tty.blog.interceptor.user.UserLogin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserConfig implements WebMvcConfigurer {
    private static final String BLOGUSERPATH = "/api/user";
    @Bean
    public UserLogin userLogin() {
        return new UserLogin();
    }
    @Bean
    public TokenLogin tokenLogin() {
        return new TokenLogin();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //用户登录
        registry.addInterceptor(this.userLogin())
                .addPathPatterns(BLOGUSERPATH + "/login");
        registry.addInterceptor(this.tokenLogin())
                .addPathPatterns(BLOGUSERPATH + "/token");
    }
}
