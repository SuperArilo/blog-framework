package com.tty.blog.service.impl;

import com.auth0.jwt.JWT;
import com.tty.blog.enums.MailType;
import com.tty.blog.service.SendMailService;
import com.tty.common.enums.redis.FindPasswordKey;
import com.tty.common.enums.redis.mail.Register;
import com.tty.common.mapper.BlogUserMapper;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.RedisUtil;
import com.tty.common.vo.BlogUserVO;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SendMailServiceImpl implements SendMailService {

    private static final Logger logger = Logger.getLogger(SendMailServiceImpl.class);

    @Resource
    RedisUtil redisUtil;
    @Resource
    FunctionTool functionTool;
    @Resource
    JavaMailSender javaMailSender;
    @Resource
    BlogUserMapper blogUserMapper;

    @Value("${mail.from}")
    private String from;
    @Value("${mail.register.subject}")
    private String registerSub;
    @Value("${mail.modify-mail.subject}")
    private String modifyMailSub;
    @Value("${mail.findPassword-mail.subject}")
    private String findPasswordSub;
    @Value("${mail.register.text-html}")
    private String registerTextHtml;
    @Value("${mail.modify-mail.text-html}")
    private String modifyMailTextHtml;
    @Value("${mail.findPassword-mail.text-html}")
    private String findPasswordTextHtml;

    @Override
    public String sendRegisterMail(String targetMail) {
        String key = this.functionTool.buildRedisKey(true,
                Register.Main.getKey(),
                Register.Register.getKey(),
                targetMail);
        //检查是否已经发送过
        if(this.redisUtil.get(key + this.functionTool.buildRedisKey(false, Register.Sent.getKey())) != null) return "已经发送邮件咯，请耐心等待一下";

        //构建注册码
        List<Object> code = this.getCode();
        //准备发送的邮箱实例
        MimeMessage message = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSentDate(new Date());
            helper.setSubject(this.registerSub);
            helper.setText(this.setCode(
                    code,
                    new BufferedReader(
                            new InputStreamReader(
                                    new ClassPathResource(this.registerTextHtml).getInputStream()))
                            .lines().parallel().collect(Collectors.joining(System.lineSeparator()))
            ), true);
            helper.setFrom(this.from);
            helper.setTo(targetMail);
            this.javaMailSender.send(message);
        } catch (Exception e) {
            logger.error(e, e);
            return "邮件发送异常，请联系管理员";
        }

        //创建code到redis
        this.redisUtil.set(
                key + this.functionTool.buildRedisKey(false, Register.Code.getKey()),
                code,
                3L,
                TimeUnit.MINUTES);
        //创建已经发送状态到redis
        this.redisUtil.set(
                key + this.functionTool.buildRedisKey(false, Register.Sent.getKey()),
                true,
                1,
                TimeUnit.MINUTES
        );
        return null;
    }

    @Override
    public String sendModifyEmail(String targetMail, HttpServletRequest request) {
        String previousToken = request.getHeader("token");
        Long uid = JWT.decode(previousToken).getClaim("uid").asLong();
        if (blogUserMapper.queryHaveUser(targetMail, null) != null) return "该邮箱已经被注册！";
        if (redisUtil.get(targetMail + "_code_modify_email_sent") != null) return "已经发送邮件咯，请耐心等待一下";
        redisUtil.delete(uid + "_code_modify_email");
        List<Object> code = getCode();
        StringBuilder builder =  new StringBuilder();
        code.forEach(builder::append);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSentDate(new Date());
            helper.setSubject(this.modifyMailSub);
            helper.setText(setCode(
                            Arrays.asList(request.getRequestURL().toString().replaceAll(request.getRequestURI(), ""),
                                    previousToken,
                                    builder.toString()),
                            new BufferedReader(
                                    new InputStreamReader(
                                            new ClassPathResource(this.modifyMailTextHtml).getInputStream()))
                                    .lines().parallel().collect(Collectors.joining(System.lineSeparator()))),
                    true);
            helper.setFrom(this.from);
            helper.setTo(targetMail);
            javaMailSender.send(message);
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("email", targetMail);
            redisUtil.set(uid + "_code_modify_email", map, 3,TimeUnit.MINUTES);
            redisUtil.set(targetMail + "_code_modify_email_sent", targetMail, 1,TimeUnit.MINUTES);
        } catch (Exception exception) {
            return "邮件发送异常，请联系管理员";
        }
        return null;
    }

    @Override
    public String sendFindPasswordEmail(String targetEmail, HttpServletRequest request) {
        String key = this.functionTool.buildRedisKey(true, FindPasswordKey.Main.getKey(), targetEmail);
        String codeKey = this.functionTool.buildRedisKey(false, FindPasswordKey.Code.getKey());
        if (redisUtil.get(key + codeKey) != null) return "已经发送过邮件了，请耐心等待一下";
        BlogUserVO userVO = blogUserMapper.queryHaveUser(targetEmail, null);
        List<Object> code = this.getCode();
        if (userVO == null) {
            redisUtil.set(key + codeKey, code, 3,TimeUnit.MINUTES);
            return null;
        }
        StringBuilder builder =  new StringBuilder();
        code.forEach(builder::append);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSentDate(new Date());
            helper.setSubject(this.findPasswordSub);
            helper.setText(setCode(
                    List.of(request.getRequestURL().toString().replaceAll(request.getRequestURI(), ""),
                            builder.toString(),
                            targetEmail),
                    new BufferedReader(
                            new InputStreamReader(
                                    new ClassPathResource(this.findPasswordTextHtml).getInputStream()))
                            .lines().parallel().collect(Collectors.joining(System.lineSeparator()))), true);
            helper.setFrom(this.from);
            helper.setTo(targetEmail);
            javaMailSender.send(message);
            redisUtil.set(key + codeKey, code, 3,TimeUnit.MINUTES);
        } catch (Exception exception){
            logger.error(exception, exception);
            return "邮件发送异常，请联系管理员";
        }
        return null;
    }

    @Override
    public boolean verify(String targetMail, String code, MailType type) {
        String key;
        switch (type) {
            case Register:
                key = this.functionTool.buildRedisKey(true,
                        Register.Main.getKey(),
                        Register.Register.getKey());
                boolean equals = code.equals(this.buildCode(key + this.functionTool.buildRedisKey(false, targetMail,Register.Code.getKey())));
                if (equals) {
                    Set<String> keys = this.redisUtil.keys(key + this.functionTool.buildRedisKey(false, targetMail) + "*");
                    if(!keys.isEmpty()) {
                        keys.forEach(item -> this.redisUtil.delete(item));
                    }
                }
                return equals;
            case ModifyMail:
                break;
            case ModifyPassword:
                System.out.println(114514);
                break;
        }
        return false;
    }

    protected List<Object> getCode() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add((int) (Math.random() * 10));
        }
        return list;
    }
    protected String setCode(List<Object> code, String content){
        for(int index = 0;index < code.size();index++){
            content = content.replaceAll("code_" + index, code.get(index).toString());
        }
        return content;
    }

    //构建从redis 里获取的 code
    protected String buildCode(String redisKey) {
        StringBuilder sb = new StringBuilder();
        Object codeList = redisUtil.get(redisKey);
        if (codeList instanceof List<?>) {
            for (Object o : (List<?>) codeList) {
                sb.append(o);
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
