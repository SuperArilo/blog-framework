package com.tty.system.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.Set;

@Component
@Order(1)
public class InvalidRequestFilter implements Filter {

    private static final Set<String> ALLOWED_METHODS = Set.of("GET", "POST", "PUT", "DELETE");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        //过滤非法HTTP请求
        if (!ALLOWED_METHODS.contains(req.getMethod())) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        //过滤非法静态资源请求
        if (req.getRequestURI().contains(".git")) {
            ((HttpServletResponse) servletResponse).sendError(HttpStatus.FORBIDDEN.value());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
