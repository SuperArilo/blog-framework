package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.upload.FileUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    private static final String BLOGFILEUPLOADPATH = "/api/upload";
    @Bean
    public FileUpload fileUpload() {
        return new FileUpload();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.fileUpload())
                .addPathPatterns(BLOGFILEUPLOADPATH + "/image");
    }
}