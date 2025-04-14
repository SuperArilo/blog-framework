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
    @Bean
    public ServletServerContainerFactoryBean containerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);  //文本消息最大缓存
        container.setMaxBinaryMessageBufferSize(8192);  //二进制消息大战缓存
        container.setMaxSessionIdleTimeout(3L * 60 * 1000); // 最大闲置时间，3分钟没动自动关闭连接
        container.setAsyncSendTimeout(10L * 1000); //异步发送超时时间
        return container;
    }
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(this.socketHandler(), this.basePathProperties + "/ws/notice")
                .addInterceptors(this.blogSocketHandshake())
                .setAllowedOrigins("*");
    }
}
