package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.entity.BlogVisitor;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;

public interface BlogVisitorService extends IService<BlogVisitor> {
    JsonResult visitorList(PageUtil pageUtil);
}
