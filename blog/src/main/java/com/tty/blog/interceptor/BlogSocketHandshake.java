package com.tty.blog.interceptor;

import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class BlogSocketHandshake extends HttpSessionHandshakeInterceptor {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest)) return false;
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String token = queryParams.getFirst("token");
        if (token == null || token.isEmpty()) return false;
        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler webSocketHandler, Exception e) {
        super.afterHandshake(request, response, webSocketHandler, e);
    }
}
