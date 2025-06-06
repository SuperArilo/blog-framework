package com.tty.blog.config;

import com.tty.blog.config.handler.BlogWebSocket;
import com.tty.blog.interceptor.BlogSocketHandshake;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public WebSocketHandler socketHandler() {
        return new BlogWebSocket();
    }

    @Bean
    public BlogSocketHandshake blogSocketHandshake() {
        return new BlogSocketHandshake();
    }
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(this.socketHandler(), this.basePathProperties.basePath + "/ws/notice")
                .addInterceptors(this.blogSocketHandshake())
                .setAllowedOrigins("*");
    }
}
