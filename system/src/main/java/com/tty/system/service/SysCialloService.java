package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysCiallo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SysCialloService extends IService<SysCiallo> {
    JsonResult cialloAdd(String title,
                         MultipartFile imageFile,
                         MultipartFile audioFile,
                         HttpServletRequest request);
}
