package com.tty.system.utils;

import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.SpringBeanUtil;
import com.tty.system.entity.SysUserEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class UserUtil {

    /**
     * token
     */
    private static final String TOKEN = "token";

    /**
     * 获取用户信息
     * @return 用户信息
     */
    public static SysUserEntity getUserInfo() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                .map(ServletRequestAttributes::getRequest)
                .map(request -> {
                    String token = request.getHeader(TOKEN);
                    JsonWebTokenUtil jsonWebTokenUtil = SpringBeanUtil.getBean(JsonWebTokenUtil.class);
                    try {
                        jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.SYSTEM);
                    } catch (Exception e) {
                        return null;
                    }
                    return jsonWebTokenUtil.getPayLoad(token, SysUserEntity.class);
                })
                .orElse(null);
    }
}
