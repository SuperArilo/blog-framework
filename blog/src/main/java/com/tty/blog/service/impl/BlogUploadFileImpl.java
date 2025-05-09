package com.tty.blog.service.impl;

import com.auth0.jwt.JWT;
import com.tty.blog.service.BlogUploadFileService;
import com.tty.common.enums.ImageType;
import com.tty.common.utils.JsonResult;
import com.tty.system.service.SysManagerFileService;
import com.tty.system.utils.ImageConvertUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class BlogUploadFileImpl implements BlogUploadFileService {

    @Resource
    private SysManagerFileService managerFileService;
    @Resource
    private ImageConvertUtil imageConvertUtil;
    @Value("${cdn.path}")
    protected String path;

    @Override
    public JsonResult uploadFile(MultipartFile file, HttpServletRequest request) {
        String fileSuffix = this.imageConvertUtil.imageFileCheck(file);
        if (fileSuffix == null) return JsonResult.ERROR(-1,"图片格式不匹配");
        String imageUrl = managerFileService.uploadImageByBunny(
                file,
                UUID.randomUUID() + "." + fileSuffix,
                this.path,
                JWT.decode(request.getHeader("token")).getClaim("uid").asLong(),
                ImageType.Normal);
        return imageUrl == null ? JsonResult.ERROR(-1, "文件写入失败！"):JsonResult.OK("上传成功", imageUrl);
    }
}
