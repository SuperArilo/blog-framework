package com.tty.blog.service.impl;

import cn.hutool.captcha.CircleCaptcha;
import com.tty.blog.service.CaptchaService;
import com.tty.common.entity.CaptchaDto;
import com.tty.common.enums.captcha.Captcha;
import com.tty.common.enums.captcha.CaptchaType;
import com.tty.common.utils.CaptchaUtil;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.IpUtil;
import com.tty.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger logger = Logger.getLogger(CaptchaServiceImpl.class);

    @Resource
    CaptchaUtil captchaUtil;
    @Resource
    FunctionTool functionTool;
    @Resource
    RedisUtil redisUtil;

    @Override
    public void imageProcess(String type, HttpServletRequest request, HttpServletResponse response) {
        CaptchaType captchaType;
        String ip = IpUtil.getIpAdder(request);
        try {
            captchaType = CaptchaType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            logger.error(e, e);
            this.functionTool.setResponse(response, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.value(),"Why are doing this?");
            return;
        }
        CaptchaDto dto = (CaptchaDto) this.redisUtil.get(this.functionTool.buildRedisKey(false, Captcha.Main.getKey(), Captcha.Image.getKey(), ip));
        CircleCaptcha circleCaptcha = captchaUtil.createToImage();
        if (dto == null) {
            dto = new CaptchaDto();
            dto.setCaptchaUUID(UUID.randomUUID());
            dto.setCode(circleCaptcha.getCode());
            dto.setCaptchaType(captchaType);
            try(ServletOutputStream outputStream = response.getOutputStream()) {
                circleCaptcha.write(outputStream);
            } catch (Exception e) {
                logger.error(e, e);
            }
        } else {
            dto.setCode(circleCaptcha.getCode());
            try(ServletOutputStream outputStream = response.getOutputStream()) {
                circleCaptcha.write(outputStream);
            } catch (Exception e) {
                logger.error(e, e);
            }
        }
        this.redisUtil.set(this.functionTool.buildRedisKey(false, Captcha.Main.getKey(), Captcha.Image.getKey(), ip), dto, 1L, TimeUnit.MINUTES);
    }

    @Override
    public String base64Process(String uuid, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
