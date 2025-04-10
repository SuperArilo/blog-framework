package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogNotice;
import com.tty.blog.event.PushNoticeEvent;
import com.tty.blog.mapper.BlogNoticeMapper;
import com.tty.blog.service.BlogNoticeService;
import com.tty.common.entity.TokenUser;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogNoticeServiceImpl extends ServiceImpl<BlogNoticeMapper, BlogNotice> implements BlogNoticeService {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    ApplicationEventPublisher publisher;
    @Override
    public JsonResult queryNoticeList(PageUtil pageUtil, Integer classType, HttpServletRequest request) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(this.baseMapper.queryNoticeByUid(classType, this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId()))));
    }

    @Override
    public JsonResult userReadNotice(List<Long> noticeIds, HttpServletRequest request) {
        int size = noticeIds.size();
        if (size == 0) return JsonResult.ERROR(400, "提交到服务器的参数为空");
        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        if (this.baseMapper.queryNoticeIsHave(noticeIds) != size) return JsonResult.ERROR(0, "服务器查询到某些通知不存在");
        if (size == this.baseMapper.readNotice(noticeIds, user.getId())) {
            this.publisher.publishEvent(new PushNoticeEvent(this, user.getId()));
            return JsonResult.OK("操作成功");
        } else {
            return JsonResult.ERROR(500, "发生未知错误");
        }
    }

    @Override
    public void createNoticeToNewUser(Long uid) {

    }
}
