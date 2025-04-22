package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.entity.BlogCiallo;
import com.tty.common.utils.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SysCialloService extends IService<BlogCiallo> {
    JsonResult cialloAdd(String title,
                         MultipartFile imageFile,
                         MultipartFile audioFile,
                         HttpServletRequest request);
}
