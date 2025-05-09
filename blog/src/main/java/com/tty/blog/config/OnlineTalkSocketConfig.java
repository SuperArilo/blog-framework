package com.tty.blog.config;

import com.tty.blog.config.handler.OnlineTalkSocketHandler;
import com.tty.blog.interceptor.OnlineTalkSocketHandshake;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
public class OnlineTalkSocketConfig implements WebSocketConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public OnlineTalkSocketHandler onlineTalkHandler() {
        return new OnlineTalkSocketHandler();
    }

    @Bean
    public OnlineTalkSocketHandshake onlineTalkHandshake() {
        return new OnlineTalkSocketHandshake();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.onlineTalkHandler(), this.basePathProperties.basePath + "/ws/minecraft/online")
                .addInterceptors(this.onlineTalkHandshake())
                .setAllowedOrigins("*");
    }
}
