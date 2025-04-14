package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.mail.Mail;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MailConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public Mail mail() {
        return new Mail();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.mail())
                .addPathPatterns()
                .excludePathPatterns(
                        this.basePathProperties.basePath + "/mail/register",
                        this.basePathProperties.basePath + "/mail/modify/email",
                        this.basePathProperties.basePath + "/mail/find-password"
                );
    }
}
