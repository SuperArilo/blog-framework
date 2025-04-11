package com.tty.system.interceptor;

import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.RedisUtil;
import com.tty.system.entity.SysUserEntity;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    FunctionTool functionTool;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String token = request.getHeader("token");
        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.SYSTEM);
        } catch (Exception e) {
            this.functionTool.setResponse(response, 200, -1, "token 无效");
            return false;
        }
        SysUserEntity payLoad = this.jsonWebTokenUtil.getPayLoad(token, SysUserEntity.class);
        if (this.redisUtil.get("overdue_uid_" + payLoad.getId()) != null) {
            this.functionTool.setResponse(response, 403, -1, "token无效");
            return false;
        }
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
