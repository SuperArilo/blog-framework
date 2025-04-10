package com.tty.blog.service.impl;

import com.tty.blog.enums.MailType;
import com.tty.blog.service.SendMailService;
import com.tty.common.enums.redis.mail.Register;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.RedisUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

    @Value("${mail.from}")
    private String from;
    @Value("${mail.register.subject}")
    private String registerSub;
    @Value("${mail.modify-mail.subject}")
    private String modifyMailSub;
    @Value("${mail.register.text-html}")
    private String registerTextHtml;
    @Value("${mail.modify-mail.text-html}")
    private String modifyMailTextHtml;

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
                                    new ClassPathResource(this.registerTextHtml).getInputStream()
                            )
                    ).lines().parallel().collect(Collectors.joining(System.lineSeparator()))
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
        return null;
    }

    @Override
    public String sendFindPasswordEmail(String targetEmail, HttpServletRequest request) {
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
