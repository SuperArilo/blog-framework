package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.service.BlogCialloService;
import com.tty.common.entity.BlogCiallo;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.ImageType;
import com.tty.common.mapper.BlogCialloMapper;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import com.tty.system.service.SysManagerFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class BlogCialloServiceImpl extends ServiceImpl<BlogCialloMapper, BlogCiallo> implements BlogCialloService {

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    SysManagerFileService sysManagerFileService;

    @Override
    public JsonResult serviceList(PageUtil pageUtil) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(this.baseMapper.getLists())));
    }

    @Override
    public JsonResult cialloAdd(String title, MultipartFile imageFile, MultipartFile audioFile, HttpServletRequest request) {
        Long id = jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId();
        BlogCiallo ciallo = new BlogCiallo();
        ciallo.setTitle(title);
        String imageUrl = sysManagerFileService.uploadImageByBunny(imageFile, UUID.randomUUID().toString(), "", id, ImageType.Normal);
        String audioUrl = sysManagerFileService.uploadFileByBunny(audioFile, UUID.randomUUID().toString(), "/audio", id);
        if (imageUrl == null || audioUrl == null) {
            return JsonResult.ERROR(0, "文件上传出错");
        }
        ciallo.setImageUrl(imageUrl);
        ciallo.setAudioUrl(audioUrl);
        ciallo.setAuthor(id);
        this.save(ciallo);
        return JsonResult.OK("添加成功");
    }
}
