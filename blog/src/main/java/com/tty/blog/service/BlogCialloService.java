package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogCiallo;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BlogCialloService extends IService<BlogCiallo> {
    JsonResult serviceList(PageUtil pageUtil);
    JsonResult cialloAdd(String title,
                         MultipartFile imageFile,
                         MultipartFile audioFile,
                         HttpServletRequest request);
}
