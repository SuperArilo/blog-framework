package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.file.ResourceFile;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ResourceFileConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public ResourceFile resourceFile() {
        return new ResourceFile();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.resourceFile())
                .addPathPatterns(this.basePathProperties.basePath + "/file/**");
    }
}
