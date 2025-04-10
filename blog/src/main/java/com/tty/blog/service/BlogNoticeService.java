package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogNotice;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlogNoticeService extends IService<BlogNotice> {
    JsonResult queryNoticeList(PageUtil pageUtil, Integer classType, HttpServletRequest request);
    JsonResult userReadNotice(List<Long> noticeIds,
                              HttpServletRequest request);
    void createNoticeToNewUser(Long uid);
}
