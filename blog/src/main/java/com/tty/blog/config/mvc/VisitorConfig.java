package com.tty.blog.config.mvc;


import com.tty.blog.interceptor.visitor.Visitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class VisitorConfig implements WebMvcConfigurer {
    private static final String VISITORPATH = "/api/visitor";
    @Bean
    public Visitor visitor() {
        return new Visitor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.visitor())
                .addPathPatterns("")
                .excludePathPatterns(VISITORPATH + "/list");
    }
}