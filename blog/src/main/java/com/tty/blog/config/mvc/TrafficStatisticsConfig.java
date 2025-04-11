package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.TrafficStatistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class TrafficStatisticsConfig implements WebMvcConfigurer {
    @Bean
    public TrafficStatistics trafficStatistics() {
        return new TrafficStatistics();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.trafficStatistics())
                .addPathPatterns("/**");
    }
}
