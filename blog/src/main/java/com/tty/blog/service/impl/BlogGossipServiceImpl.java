package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogGossip;
import com.tty.blog.entity.BlogGossipCommentLike;
import com.tty.blog.entity.BlogGossipLike;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.mapper.BlogGossipCommentMapper;
import com.tty.blog.mapper.BlogGossipMapper;
import com.tty.blog.service.BlogGossipCommentLikeService;
import com.tty.blog.service.BlogGossipLikeService;
import com.tty.blog.service.BlogGossipService;
import com.tty.blog.service.BlogUserService;
import com.tty.blog.vo.BlogCommentVO;
import com.tty.blog.vo.gossip.GossipVO;
import com.tty.blog.vo.gossip.InstanceVO;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import com.tty.common.utils.TimeFormat;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogGossipServiceImpl extends ServiceImpl<BlogGossipMapper, BlogGossip> implements BlogGossipService {

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    BlogUserService blogUserService;
    @Resource
    BlogGossipLikeService blogGossipLikeService;
    @Resource
    BlogGossipCommentMapper blogGossipCommentMapper;
    @Resource
    BlogGossipCommentLikeService blogGossipCommentLikeService;

    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;

    @Override
    public GossipVO gossipListGet(PageUtil pageUtil, Long viewUid, Long targetId, Long commentId, HttpServletRequest request) {
        String token = request.getHeader("token");
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        List<InstanceVO> dataList = this.baseMapper.gossipListGet(viewUid);
        //检查token是否合法
        boolean tokenStatus;
        Long uid = null;
        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            tokenStatus = false;
        } catch (Exception e) {
            tokenStatus = true;
        }
        if (!tokenStatus) uid = jsonWebTokenUtil.getPayLoad(token, TokenUser.class).getId();
        for (InstanceVO vo : dataList) {
            BlogUser user = this.blogUserService.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, vo.getAuthor()));
            if (user == null) {
                vo.setAvatar(this.loginOffAvatar);
                vo.setNickName("该用户已注销");
                vo.setAuthor(0L);
            } else if (user.getLoginOff()) {
                vo.setAvatar(this.loginOffAvatar);
                vo.setNickName("该用户已注销");
            }
            vo.setCreateTimeFormat(TimeFormat.timeCheck(vo.getCreateTime().getTime(), new Date().getTime()));
            if (!tokenStatus) {
                vo.setLike(this.blogGossipLikeService.getOne(
                        new QueryWrapper<BlogGossipLike>().lambda()
                                .eq(BlogGossipLike::getGossipId, vo.getId())
                                .eq(BlogGossipLike::getUid, uid)) != null);
            }
        }

        //设置选项参数target
        GossipVO gossipVO = new GossipVO();
        gossipVO.setInstance(new PageUtil(new PageInfo<>(dataList)));
        InstanceVO one = this.baseMapper.queryGossipById(targetId);
        if (one != null && !tokenStatus) {
            one.setLike(this.blogGossipLikeService.getOne(
                    new QueryWrapper<BlogGossipLike>().lambda()
                            .eq(BlogGossipLike::getGossipId, one.getId())
                            .eq(BlogGossipLike::getUid, uid)) != null);
        }

        gossipVO.setTargetGossip(one);
        BlogCommentVO commentVO = this.blogGossipCommentMapper.queryCommentByGossipIdAndCommentId(targetId, commentId);
        if (commentVO != null) {
            commentVO.setLike(this.blogGossipCommentLikeService.getOne(
                    new QueryWrapper<BlogGossipCommentLike>().lambda()
                            .eq(BlogGossipCommentLike::getCommentId, commentId)
                            .eq(BlogGossipCommentLike::getUid, uid)) != null);
            commentVO.setCreateTimeFormat(TimeFormat.timeCheck(commentVO.getCreateTime().getTime(), new Date().getTime()));
        }
        gossipVO.setTargetComment(commentVO);
        return gossipVO;
    }

    @Override
    public JsonResult gossipLike(Long gossipId, HttpServletRequest request) {
        if (this.getOne(new QueryWrapper<BlogGossip>().lambda().eq(BlogGossip::getId, gossipId)) == null) return JsonResult.ERROR(404, "服务器没有找到相应的碎语");
        Long uid = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId();
        Map<String, Object> likeStatus = new HashMap<>();
        if (this.blogGossipLikeService.remove(new QueryWrapper<BlogGossipLike>().lambda().eq(BlogGossipLike::getGossipId, gossipId).eq(BlogGossipLike::getUid, uid))) {
            likeStatus.put("status", false);
            likeStatus.put("likes", this.blogGossipLikeService.count(new QueryWrapper<BlogGossipLike>().lambda().eq(BlogGossipLike::getGossipId, gossipId)));
            return JsonResult.OK("取消喜欢了咯 (´。＿。｀)", likeStatus);
        } else {
            BlogGossipLike blogGossipLike = new BlogGossipLike();
            blogGossipLike.setGossipId(gossipId);
            blogGossipLike.setUid(uid);
            this.blogGossipLikeService.save(blogGossipLike);
            likeStatus.put("status", true);
            likeStatus.put("likes", this.blogGossipLikeService.count(new QueryWrapper<BlogGossipLike>().lambda().eq(BlogGossipLike::getGossipId, gossipId)));
            //发送通知到对应的用户
            return JsonResult.OK("喜欢成功，感谢你的点赞 (○｀ 3′○)", likeStatus);
        }
    }

    @Override
    public JsonResult gossipCreateByUser(String content, HttpServletRequest request) {
        BlogGossip blogGossip = new BlogGossip();
        blogGossip.setAuthor(this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId());
        blogGossip.setCreateTime(new Date());
        blogGossip.setContent(content);
        this.save(blogGossip);
        return JsonResult.OK("发表成功 (￣y▽,￣)╭ ");
    }

    @Override
    public JsonResult gossipDeleteByUser(Long gossipId, HttpServletRequest request) {
        if (this.baseMapper.deleteGossipById(gossipId, this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId())) {
            return JsonResult.OK("删除成功");
        } else {
            return JsonResult.ERROR(400, "服务器没有查询到该内容");
        }
    }
}
