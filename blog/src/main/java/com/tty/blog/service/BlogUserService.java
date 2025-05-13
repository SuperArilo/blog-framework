package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.dto.BlogModifyEmailDTO;
import com.tty.common.dto.BlogUserProfilesModifyDTO;
import com.tty.common.entity.BlogUser;
import com.tty.common.utils.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface BlogUserService extends IService<BlogUser> {
    JsonResult registerUser(String email,
                            String password,
                            String nickName,
                            String verifyCode,
                            HttpServletResponse response);
    JsonResult login(String email,
                     String password,
                     HttpServletRequest request);

    JsonResult token(HttpServletRequest request);
    JsonResult findPasswordVerify(String email,
                                  String verifyCode);
    JsonResult passwordModify(String verifyUUID,
                              String email,
                              String password,
                              String passwordAgain);
    ResponseEntity<byte[]> userAvatar(Long uid);
    JsonResult loginOutUser(HttpServletRequest request);
    JsonResult userProfiles(Long viewUid, HttpServletRequest request);
    JsonResult userProfilesModify(BlogUserProfilesModifyDTO modifyDTO,
                                  HttpServletRequest request);
    JsonResult userProfilesModifyEmail(BlogModifyEmailDTO emailDTO, HttpServletRequest request);
}
