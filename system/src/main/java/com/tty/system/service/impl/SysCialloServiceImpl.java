package com.tty.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.ImageType;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.system.entity.SysCiallo;
import com.tty.system.mapper.SysCialloMapper;
import com.tty.system.service.SysCialloService;
import com.tty.system.service.SysManagerFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class SysCialloServiceImpl extends ServiceImpl<SysCialloMapper, SysCiallo> implements SysCialloService {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    SysManagerFileService sysManagerFileService;
    @Override
    public JsonResult cialloAdd(String title, MultipartFile imageFile, MultipartFile audioFile, HttpServletRequest request) {
        Long uid = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId();
        SysCiallo ciallo = new SysCiallo();
        ciallo.setTitle(title);
        String imageUrl = sysManagerFileService.uploadImageByBunny(imageFile, UUID.randomUUID().toString(), "", uid, ImageType.Normal);
        String audioUrl = sysManagerFileService.uploadFileByBunny(audioFile, UUID.randomUUID().toString(), "/audio", uid);
        if (imageUrl == null || audioUrl == null) {
            return JsonResult.ERROR(0, "文件上传出错");
        }
        ciallo.setImageUrl(imageUrl);
        ciallo.setAudioUrl(audioUrl);
        ciallo.setAuthor(uid);
        ciallo.setCreateTime(new Date());
        this.save(ciallo);
        return JsonResult.OK("添加成功");

    }
}
