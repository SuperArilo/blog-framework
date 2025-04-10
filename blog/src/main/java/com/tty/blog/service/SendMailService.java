package com.tty.blog.service;

import com.tty.blog.enums.MailType;
import jakarta.servlet.http.HttpServletRequest;

public interface SendMailService {
    /**
     *
     * @param targetMail 目标邮箱地址
     * @return 成功发送会返回 null
     */
    String sendRegisterMail(String targetMail);
    String sendModifyEmail(String targetMail, HttpServletRequest request);
    String sendFindPasswordEmail(String targetEmail, HttpServletRequest request);
    boolean verify(String targetMail, String code, MailType type);
}
