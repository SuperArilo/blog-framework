package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.user.*;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

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
                .addPathPatterns(this.basePathProperties.basePath + "/user/login");
        registry.addInterceptor(this.tokenLogin())
                .addPathPatterns(this.basePathProperties.basePath + "/user/token");
        //用户注册
        registry.addInterceptor(this.userRegister())
                .addPathPatterns("")
                .excludePathPatterns(this.basePathProperties.basePath + "/user/register");
        //用户注销
        registry.addInterceptor(this.userLoginOut())
                .addPathPatterns(this.basePathProperties.basePath + "/user/login-out");
        //用户功能相关
        registry.addInterceptor(this.userFunction())
                .addPathPatterns(
                        this.basePathProperties.basePath + "/user/profiles/modify",
                        this.basePathProperties.basePath + "/user/profiles/modify/email"
                )
                .excludePathPatterns(
                        this.basePathProperties.basePath + "/user/profiles/find-password/verify",
                        this.basePathProperties.basePath + "/user/profiles/modify/password",
                        this.basePathProperties.basePath + "/user/profiles/view/{viewUid}"
                );
        registry.addInterceptor(this.like())
                .addPathPatterns(this.basePathProperties.basePath + "/user/like/apply")
                .excludePathPatterns(this.basePathProperties.basePath + "/user/like/list");
    }
}
