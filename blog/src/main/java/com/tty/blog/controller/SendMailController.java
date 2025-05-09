package com.tty.blog.controller;

import com.tty.blog.service.SendMailService;
import com.tty.common.enums.InputRegex;
import com.tty.common.utils.JsonResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class SendMailController {
    @Resource
    SendMailService sendMailService;
    @PostMapping("/register")
    public JsonResult sendMail(@RequestParam("mail") String mail) {
        if(mail.isEmpty()) return JsonResult.ERROR(400, "邮箱不能为空哦");
        if(!mail.trim().matches(InputRegex.Email.getRegex())) return JsonResult.ERROR(400, "邮箱格式错误");

        String message =  this.sendMailService.sendRegisterMail(mail);
        if (message != null) {
            return JsonResult.ERROR(0, message);
        }
        return JsonResult.OK("发送成功");
    }

    @PostMapping("/modify/email")
    public JsonResult blogUserModifyEmail(@RequestParam(value = "email") String email,
                                          HttpServletRequest request) {
        if (!email.trim().matches(InputRegex.Email.getRegex())) return JsonResult.ERROR(400, "邮箱格式错误");
        String message = sendMailService.sendModifyEmail(email, request);
        if (message == null) return JsonResult.OK("邮件发送成功");
        return JsonResult.ERROR(0, message);
    }

    @PostMapping("/find-password")
    public JsonResult blogFindPassword(@RequestParam("email") String email,
                                       HttpServletRequest request) {
        if (email.isEmpty()) return JsonResult.ERROR(400, "邮箱为空");
        if (!email.trim().matches(InputRegex.Email.getRegex())) return JsonResult.ERROR(400, "邮箱格式错误");
        String message = sendMailService.sendFindPasswordEmail(email, request);
        if (message != null) {
            return JsonResult.ERROR(-1, message);
        } else {
            return JsonResult.OK("邮件发送成功");
        }
    }
}
