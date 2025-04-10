package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.dto.event.LikeToUser;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.entity.BlogUserLike;
import com.tty.blog.event.UserLikeTargetUser;
import com.tty.blog.mapper.BlogUserLikeMapper;
import com.tty.blog.service.BlogUserLikeService;
import com.tty.blog.service.BlogUserService;
import com.tty.common.entity.TokenUser;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BlogUserLikeServiceImpl extends ServiceImpl<BlogUserLikeMapper, BlogUserLike> implements BlogUserLikeService {
    @Resource
    BlogUserService blogUserService;
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    ApplicationEventPublisher publisher;
    @Override
    public JsonResult likeListGet(PageUtil pageUtil, Long targetUid) {
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        return JsonResult.OK("ok", new PageUtil(new PageInfo<>(this.baseMapper.queryLikeList(targetUid))));
    }

    @Override
    public JsonResult likeTarget(Long targetUid, HttpServletRequest request) {
        if (!this.blogUserService.exists(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, targetUid))) return JsonResult.ERROR(404, "对象不存在");
        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        Map<String, Boolean> map = new HashMap<>();
        if (!this.remove(
                new QueryWrapper<BlogUserLike>().lambda()
                        .eq(BlogUserLike::getUid, user.getId())
                        .eq(BlogUserLike::getTargetUid, targetUid))) {
            BlogUserLike blogUserLike = new BlogUserLike();
            blogUserLike.setTargetUid(targetUid);
            blogUserLike.setUid(user.getId());
            blogUserLike.setCreateTime(new Date());
            this.save(blogUserLike);
            map.put("status", true);

            LikeToUser likeToUser = new LikeToUser();
            likeToUser.setUid(user.getId());
            likeToUser.setTargetUid(targetUid);
            this.publisher.publishEvent(new UserLikeTargetUser(this, likeToUser));
            return JsonResult.OK("点赞成功o(￣▽￣)ｄ", map);
        } else {
            map.put("status", false);
            return JsonResult.OK("取消点赞咯", map);
        }
    }
}
