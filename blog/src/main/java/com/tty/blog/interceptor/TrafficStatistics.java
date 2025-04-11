package com.tty.blog.interceptor;

import com.tty.blog.entity.BlogVisitor;
import com.tty.blog.mapper.BlogVisitorMapper;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Component
public class TrafficStatistics implements HandlerInterceptor {
    @Resource
    BlogVisitorMapper blogVisitorMapper;
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String token = request.getHeader("token");

        if (!StringUtils.hasLength(token)) return true;

        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
        } catch (Exception e) {
            return true;
        }

        TokenUser payLoad = this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
        Long userId = payLoad.getId();

        BlogVisitor visitor = new BlogVisitor();
        visitor.setUid(userId);
        visitor.setVisitTime(new Date());
        blogVisitorMapper.upsert(visitor);

        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}