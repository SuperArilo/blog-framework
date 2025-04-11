package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserConfig implements WebMvcConfigurer {
    private static final String BLOGUSERPATH = "/user";
    private static final String USERLIKEPATH = BLOGUSERPATH + "/like";
    @Bean
    public UserLogin userLogin() {
        return new UserLogin();
    }
    @Bean
    public UserRegister userRegister() {
        return new UserRegister();
    }
    @Bean
    public TokenLogin tokenLogin() {
        return new TokenLogin();
    }
    @Bean
    public UserLoginOut userLoginOut() {
        return new UserLoginOut();
    }
    @Bean
    public UserFunction userFunction() {
        return new UserFunction();
    }
    @Bean
    public Like like() {
        return new Like();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //用户登录
        registry.addInterceptor(this.userLogin())
                .addPathPatterns(BLOGUSERPATH + "/login");
        registry.addInterceptor(this.tokenLogin())
                .addPathPatterns(BLOGUSERPATH + "/token");
        //用户注册
        registry.addInterceptor(this.userRegister())
                .addPathPatterns("")
                .excludePathPatterns(BLOGUSERPATH + "/register");
        //用户注销
        registry.addInterceptor(this.userLoginOut())
                .addPathPatterns(BLOGUSERPATH + "/login-out");
        //用户功能相关
        registry.addInterceptor(this.userFunction())
                .addPathPatterns(
                        BLOGUSERPATH + "/profiles/modify",
                        BLOGUSERPATH + "/profiles/modify/email"
                )
                .excludePathPatterns(
                        BLOGUSERPATH + "/profiles/find-password/verify",
                        BLOGUSERPATH + "/profiles/modify/password",
                        BLOGUSERPATH + "/profiles/view/{viewUid}"
                );
        registry.addInterceptor(this.like())
                .addPathPatterns(USERLIKEPATH + "/apply")
                .excludePathPatterns(USERLIKEPATH + "/like");
    }
}
