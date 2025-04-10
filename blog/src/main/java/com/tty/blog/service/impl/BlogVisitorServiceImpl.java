package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.entity.BlogVisitor;
import com.tty.blog.mapper.BlogVisitorMapper;
import com.tty.blog.service.BlogUserService;
import com.tty.blog.service.BlogVisitorService;
import com.tty.blog.vo.BlogVisitorVO;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.common.utils.TimeFormat;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogVisitorServiceImpl extends ServiceImpl<BlogVisitorMapper, BlogVisitor> implements BlogVisitorService {
    @Resource
    BlogUserService blogUserService;
    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;
    @Override
    public JsonResult visitorList(PageUtil pageUtil) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        List<BlogVisitorVO> blogVisitorVOS = this.baseMapper.queryVisitorList();
        for (BlogVisitorVO vo : blogVisitorVOS) {
            BlogUser one = this.blogUserService.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, vo.getUid()));
            if (one == null) {
                vo.setNickName("此用户已注销");
                vo.setUid(0L);
                vo.setAvatar(this.loginOffAvatar);
            } else if (one.getLoginOff()) {
                vo.setAvatar(this.loginOffAvatar);
                vo.setNickName("此用户已注销");
            }
            vo.setVisitTimeFormat(TimeFormat.timeCheck(vo.getVisitTime().getTime(), new Date().getTime()));
        }

        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(blogVisitorVOS)));
    }
}
