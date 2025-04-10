package com.tty.common.utils;

import cn.hutool.captcha.CircleCaptcha;
import com.tty.common.entity.CaptchaDto;
import com.tty.common.entity.exception.CaptchaCodeError;
import com.tty.common.entity.exception.CaptchaCodeNotFound;
import com.tty.common.entity.exception.CaptchaHeaderError;
import com.tty.common.enums.captcha.Captcha;
import com.tty.common.enums.captcha.CaptchaHeader;
import com.tty.common.enums.captcha.CaptchaType;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptchaUtil {

    @Value("${captcha.base64-type}")
    private String base64Type;
    @Value("${captcha.width}")
    private Integer width;
    @Value("${captcha.height}")
    private Integer height;
    @Value("${captcha.code-count}")
    private Integer codeCount;
    @Value("${captcha.circle-count}")
    private Integer circleCount;

    @Resource
    RedisUtil redisUtil;
    @Resource
    FunctionTool functionTool;

    public CircleCaptcha createToImage() {
        return cn.hutool.captcha.CaptchaUtil.createCircleCaptcha(this.width, this.height, this.codeCount, this.circleCount);
    }


    public void verify(HttpServletRequest request) throws CaptchaCodeError, CaptchaCodeNotFound, CaptchaHeaderError {
        try {
            CaptchaType.valueOf(request.getHeader(CaptchaHeader.Type.getS()).toUpperCase());
        } catch (Exception e) {
            throw new CaptchaHeaderError("code header error");
        }
        CaptchaDto dto = (CaptchaDto) redisUtil.get(functionTool.buildRedisKey(false, Captcha.Main.getKey(), Captcha.Image.getKey(), IpUtil.getIpAdder(request)));
        if (dto == null) {
            throw new CaptchaCodeNotFound("code not found");
        }
        if (!dto.getCode().equalsIgnoreCase(request.getHeader(CaptchaHeader.Code.getS()))) {
            throw new CaptchaCodeError("验证码有误，请重新检查！");
        }
    }

    public String createToBase64() {
        CircleCaptcha captcha = cn.hutool.captcha.CaptchaUtil.createCircleCaptcha(this.width, this.height, this.codeCount, this.circleCount);
        return this.base64Type + captcha.getImageBase64();
    }
}