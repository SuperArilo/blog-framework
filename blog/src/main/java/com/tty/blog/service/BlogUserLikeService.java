package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.entity.BlogUserLike;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogUserLikeService extends IService<BlogUserLike> {
    JsonResult likeListGet(PageUtil pageUtil, Long targetUid);
    JsonResult likeTarget(Long targetUid, HttpServletRequest request);
}
