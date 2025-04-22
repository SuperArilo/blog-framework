package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.common.mapper.BlogGuestbookMapper;
import com.tty.blog.service.BlogGuestbookService;
import com.tty.blog.service.BlogUserService;
import com.tty.common.vo.BlogGuestbookVO;
import com.tty.common.entity.BlogGuestbook;
import com.tty.common.entity.BlogUser;
import com.tty.common.entity.TokenUser;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import com.tty.common.utils.TimeFormat;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogGuestbookServiceImpl extends ServiceImpl<BlogGuestbookMapper, BlogGuestbook> implements BlogGuestbookService {

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    BlogUserService blogUserService;

    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;
    @Override
    public JsonResult guestbookList(PageUtil pageUtil) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        List<BlogGuestbookVO> lists = this.baseMapper.guestbookList();
        for (BlogGuestbookVO vo : lists) {
            BlogUser one = this.blogUserService.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, vo.getPublisher()));
            if (one == null) {
                vo.setAvatar(this.loginOffAvatar);
                vo.setNickName("该用户已注销");
                vo.setPublisher(0L);
            } else if (one.getLoginOff()){
                vo.setAvatar(this.loginOffAvatar);
                vo.setNickName("该用户已注销");
            }
            vo.setCreateTimeFormat(TimeFormat.timeCheck(vo.getCreateTime().getTime(), new Date().getTime()));
        }
        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(lists)));
    }

    @Override
    public JsonResult addGuestbook(String content, HttpServletRequest request) {
        if(content == null || content.isEmpty() || content.equals("<p><br></p>")) return JsonResult.ERROR(0,"不能提交空白哦 ˋ( ° ▽、° )");
        BlogGuestbook blogGuestbook = new BlogGuestbook();
        blogGuestbook.setCreateTime(new Date());
        blogGuestbook.setContent(content);
        blogGuestbook.setPublisher(this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId());
        this.save(blogGuestbook);
        return JsonResult.OK( "留言成功 (￣y▽,￣)╭ ");
    }

    @Override
    public JsonResult deleteGuestbook(Long guestbookId, HttpServletRequest request) {
        if (this.remove(
                new QueryWrapper<BlogGuestbook>().lambda()
                        .eq(BlogGuestbook::getGuestbookId, guestbookId)
                        .eq(BlogGuestbook::getPublisher, this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId()))) {
            return JsonResult.OK("删除成功 v╰(￣ω￣ｏ)");
        } else {
            return JsonResult.ERROR(500, "删除失败，请联系管理员");
        }
    }
}
