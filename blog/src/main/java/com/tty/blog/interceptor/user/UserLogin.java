package com.tty.blog.interceptor.user;

import com.tty.common.entity.exception.CaptchaCodeError;
import com.tty.common.entity.exception.CaptchaCodeNotFound;
import com.tty.common.entity.exception.CaptchaHeaderError;
import com.tty.common.enums.captcha.Captcha;
import com.tty.common.utils.CaptchaUtil;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.IpUtil;
import com.tty.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserLogin implements HandlerInterceptor {
    @Resource
    RedisUtil redisUtil;
    @Resource
    CaptchaUtil captchaUtil;
    @Resource
    FunctionTool functionTool;
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        try {
            captchaUtil.verify(request);
        } catch (CaptchaCodeError e) {
            functionTool.setResponse(response, HttpStatus.BAD_REQUEST.value(), 0, e.getMessage());
            return false;
        } catch (CaptchaCodeNotFound e) {
            functionTool.setResponse(response, HttpStatus.NOT_FOUND.value(), 0, e.getMessage());
            return false;
        } catch (CaptchaHeaderError e) {
            functionTool.setResponse(response, HttpStatus.FORBIDDEN.value(), 0, e.getMessage());
            return false;
        }
        redisUtil.delete(functionTool.buildRedisKey(false, Captcha.Main.getKey(), Captcha.Image.getKey(), IpUtil.getIpAdder(request)));
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
