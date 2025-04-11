package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.file.ResourceFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ResourceFileConfig implements WebMvcConfigurer {
    @Bean
    public ResourceFile resourceFile() {
        return new ResourceFile();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.resourceFile())
                .addPathPatterns("/file/**");
    }
}
