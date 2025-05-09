package com.tty.blog.service;

import com.tty.common.utils.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BlogUploadFileService {
    JsonResult uploadFile(MultipartFile file, HttpServletRequest request);
}
