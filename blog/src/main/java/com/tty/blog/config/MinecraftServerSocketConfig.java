package com.tty.blog.config;

import com.tty.blog.config.handler.MinecraftServerSocketHandler;
import com.tty.blog.interceptor.MinecraftServerSocketHandshake;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Slf4j
@Configuration
@EnableWebSocket
public class MinecraftServerSocketConfig implements WebSocketConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

    @Bean
    public MinecraftServerSocketHandshake minecraftServerHandshake() {
        return new MinecraftServerSocketHandshake();
    }

    @Bean
    public MinecraftServerSocketHandler minecraftServerHandler() {
        return new MinecraftServerSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(this.minecraftServerHandler(), this.basePathProperties.basePath + "/ws/minecraft/server")
                .addInterceptors(this.minecraftServerHandshake())
                .setAllowedOrigins("*");
    }
}
