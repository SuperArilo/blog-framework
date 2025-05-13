package com.tty.blog.controller;

import com.tty.common.dto.BlogModifyEmailDTO;
import com.tty.common.dto.BlogUserProfilesModifyDTO;
import com.tty.blog.service.BlogUserService;
import com.tty.common.utils.JsonResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class BlogUserController {
    @Resource
    BlogUserService blogUserService;
    @PostMapping("/register")
    public JsonResult blogUserRegister(@RequestParam(value = "email") String email,
                                       @RequestParam(value = "password") String password,
                                       @RequestParam(value = "nickName") String nickName,
                                       @RequestParam(value = "verifyCode") String verifyCode,
                                       HttpServletResponse response){
        return blogUserService.registerUser(email, password, nickName, verifyCode, response);
    }
    @PostMapping("/login")
    public JsonResult blogUserLogin(@RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "password", required = false) String password,
                                    HttpServletRequest request) {
        return blogUserService.login(email, password, request);
    }
    @GetMapping("/avatar/{uid}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable("uid") Long uid) {
        return this.blogUserService.userAvatar(uid);
    }
    @PostMapping("/token")
    public JsonResult tokenLogin(HttpServletRequest request) {
        return blogUserService.token(request);
    }

    @PostMapping("/login-out")
    public JsonResult blogUserLoginOut(HttpServletRequest request) {
        return blogUserService.loginOutUser(request);
    }

    @GetMapping("/profiles/view/{viewUid}")
    public JsonResult blogUserProfiles(@PathVariable("viewUid") Long viewUid,
                                       HttpServletRequest request) {
        return blogUserService.userProfiles(viewUid, request);
    }
    @PutMapping("/profiles/modify")
    public JsonResult blogUserProfilesModify(BlogUserProfilesModifyDTO modifyDTO,
                                             HttpServletRequest request) {
        return blogUserService.userProfilesModify(modifyDTO, request);
    }
    @PostMapping("/profiles/modify/email")
    public JsonResult blogUserModifyEmail(BlogModifyEmailDTO emailDTO,
                                          HttpServletRequest request) {
        return blogUserService.userProfilesModifyEmail(emailDTO, request);
    }
    @PostMapping("/profiles/find-password/verify")
    public JsonResult blogFindPassword(@RequestParam("verifyCode") String verifyCode,
                                       @RequestParam("email") String email) {
        return blogUserService.findPasswordVerify(email, verifyCode);
    }
    @PutMapping("/profiles/modify/password")
    public JsonResult blogModifyPassword(@RequestParam("verifyUUID") String verifyUUID,
                                         @RequestParam("email") String email,
                                         @RequestParam("password") String password,
                                         @RequestParam("passwordAgain") String passwordAgain){
        return blogUserService.passwordModify(verifyUUID, email, password, passwordAgain);
    }
}
