package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogGuestbook;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogGuestbookService extends IService<BlogGuestbook> {
    JsonResult guestbookList(PageUtil pageUtil);
    JsonResult addGuestbook(String content, HttpServletRequest request);
    JsonResult deleteGuestbook(Long guestbookId, HttpServletRequest request);
}
