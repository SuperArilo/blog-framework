package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.mail.Mail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MailConfig implements WebMvcConfigurer {
    private static final String MAILPATH = "/api/mail";
    @Bean
    public Mail mail() {
        return new Mail();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.mail())
                .addPathPatterns()
                .excludePathPatterns(
                        MAILPATH + "/register",
                        MAILPATH + "/modify/email",
                        MAILPATH + "/find-password"
                );
    }
}
