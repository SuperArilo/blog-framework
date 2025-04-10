package com.tty.system.service.impl;

import com.tty.common.constant.RedisKeyConstant;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.DateUtil;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.RedisUtil;
import com.tty.system.entity.SysUserEntity;
import com.tty.system.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tty.system.service.SysLoginService;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Resource
    private RedisUtil redisUtil;

    @Value("${jwt.token.seconds.login}")
    private int loginTokenSeconds;

    @Override
    public JsonResult login(SysUserEntity user) {
        JsonResult jsonResult = checkParams(user);
        if(jsonResult != null) {
            return jsonResult;
        }
        SysUserEntity userEntity = sysUserService.getUserByUsername(user.getUsername());
        if(userEntity == null || !Objects.equals(user.getUsername(), userEntity.getUsername())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "用户名不存在");
        }
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        if(!Objects.equals(password, userEntity.getPassword())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "密码不正确");
        }
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(userEntity.getId());
        tokenUser.setUsername(userEntity.getUsername());
        tokenUser.setType(JsonWebTokenTypeEnum.SYSTEM);

        // 设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, loginTokenSeconds);

        // 创建token
        String token = jsonWebTokenUtil.createToken(tokenUser, calendar.getTime(), JsonWebTokenTypeEnum.SYSTEM);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);

        // 时间格式化
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
        // 时间格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.YYYY_MM_DD_HH_MM_SS_PATTERN);
        // 格式化
        String expirationTime = zonedDateTime.format(dateTimeFormatter);
        data.put("expirationTime", expirationTime);
        // 缓存token
        redisUtil.set(RedisKeyConstant.SYS_USER_TOKEN + userEntity.getId(), token, loginTokenSeconds, TimeUnit.SECONDS);
        return JsonResult.OK(data);
    }

    @Override
    public JsonResult logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        TokenUser tokenUser = jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
        if(tokenUser != null && !StringUtils.hasLength(RedisKeyConstant.SYS_USER_TOKEN + tokenUser.getId()) ||  redisUtil.delete(token)) {
            return JsonResult.OK("退出成功");
        }
        return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "退出失败");
    }

    private JsonResult checkParams(SysUserEntity user) {
        if(!StringUtils.hasLength(user.getUsername())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "用户名不能为空");
        }
        if(!StringUtils.hasLength(user.getPassword())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "密码不能为空");
        }

        return null;
    }
}
