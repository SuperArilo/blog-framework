package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogFriend;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogFriendService extends IService<BlogFriend> {
    JsonResult friendsList(PageUtil pageUtil);
    JsonResult friendApply(String linkName,
                           String linkLocation,
                           String linkIntroduction,
                           String linkAvatar,
                           HttpServletRequest request);
}
