package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogFriend;
import com.tty.blog.mapper.BlogFriendMapper;
import com.tty.blog.service.BlogFriendService;
import com.tty.common.entity.TokenUser;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlogFriendServiceImpl extends ServiceImpl<BlogFriendMapper, BlogFriend> implements BlogFriendService {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Override
    public JsonResult friendsList(PageUtil pageUtil) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(this.baseMapper.getLists())));
    }

    @Override
    public JsonResult friendApply(String linkName, String linkLocation, String linkIntroduction, String linkAvatar, HttpServletRequest request) {
        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        if (this.baseMapper.queryUserHadApply(user.getId())) return JsonResult.OK("你已经提交过了哦");
        this.baseMapper.insertApply(linkName, linkLocation, linkIntroduction, linkAvatar, new Date(), user.getId());
        return JsonResult.OK("提交成功，请耐心等待审核");
    }
}
