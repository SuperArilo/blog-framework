package com.tty.system.config;

import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Getter
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(this.basePathProperties.getBasePath(),
                HandlerTypePredicate.forAnnotation(RestController.class));
    }
}
