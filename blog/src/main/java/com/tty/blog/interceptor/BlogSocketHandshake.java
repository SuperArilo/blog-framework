package com.tty.blog.interceptor;

import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class BlogSocketHandshake extends HttpSessionHandshakeInterceptor {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;
        String token = servletRequest.getServletRequest().getHeader("Sec-Websocket-Protocol");
        if (token.isEmpty()) return false;
        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler webSocketHandler, Exception e) {
        if ((request instanceof  ServletServerHttpRequest serverHttpRequest) && (response instanceof ServletServerHttpResponse serverHttpResponse)) {
            if (StringUtils.isNotEmpty(serverHttpRequest.getServletRequest().getHeader("Sec-WebSocket-Protocol"))) {
                serverHttpResponse.getServletResponse().addHeader("Sec-WebSocket-Protocol", ((ServletServerHttpRequest) request).getServletRequest().getHeader("Sec-WebSocket-Protocol"));
            } else {
                response.setStatusCode(HttpStatus.OK);
            }
        }
    }
}
