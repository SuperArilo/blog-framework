package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.enums.ReplyType;
import com.tty.blog.event.UserByReplyEvent;
import com.tty.common.mapper.BlogGossipCommentMapper;
import com.tty.blog.service.BlogGossipCommentLikeService;
import com.tty.blog.service.BlogGossipCommentService;
import com.tty.blog.service.BlogGossipService;
import com.tty.blog.service.BlogUserService;
import com.tty.common.vo.BlogCommentVO;
import com.tty.common.vo.commentReply.CommentByReplyUser;
import com.tty.common.vo.commentReply.CommentReplyUser;
import com.tty.common.entity.*;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.enums.NoticeSetting;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import com.tty.common.utils.TimeFormat;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogGossipCommentServiceImpl extends ServiceImpl<BlogGossipCommentMapper, BlogGossipComment> implements BlogGossipCommentService {
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    BlogUserService blogUserService;
    @Resource
    BlogGossipService blogGossipService;
    @Resource
    BlogGossipCommentLikeService blogGossipCommentLikeService;

    @Resource
    ApplicationEventPublisher eventPublisher;

    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;
    @Override
    public JsonResult commentList(PageUtil pageUtil, Long gossipId, HttpServletRequest request) {
        String token = request.getHeader("token");
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        List<BlogCommentVO> list = this.baseMapper.commentList(gossipId);
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
        for (BlogCommentVO vo : list) {
            CommentReplyUser replyUser = vo.getReplyUser();
            LambdaQueryWrapper<BlogUser> queryWrapper = new QueryWrapper<BlogUser>().lambda();
            if (replyUser == null) {
                CommentReplyUser newReplyUser = new CommentReplyUser();
                newReplyUser.setReplyUserId(0L);
                newReplyUser.setReplyAvatar(this.loginOffAvatar);
                newReplyUser.setReplyNickName("此用户已注销");
                vo.setReplyUser(newReplyUser);
            } else {
                BlogUser userVO = this.blogUserService.getOne(queryWrapper.eq(BlogUser::getUid, replyUser.getReplyUserId()));
                if (userVO == null) {
                    replyUser.setReplyAvatar(this.loginOffAvatar);
                    replyUser.setReplyNickName("此用户已注销");
                    replyUser.setReplyUserId(0L);
                }
                else if (userVO.getLoginOff()) {
                    replyUser.setReplyAvatar(this.loginOffAvatar);
                    replyUser.setReplyNickName("此用户已注销");
                }
            }
            CommentByReplyUser byReplyUser = vo.getByReplyUser();
            if (byReplyUser != null) {
                queryWrapper.clear();
                BlogUser userVO  = this.blogUserService.getOne(queryWrapper.eq(BlogUser::getUid, byReplyUser.getByReplyUserId()));
                if (userVO == null) {
                    byReplyUser.setByReplyUserId(0L);
                    byReplyUser.setByReplyAvatar(this.loginOffAvatar);
                    byReplyUser.setByReplyNickName("此用户已注销");
                } else if (userVO.getLoginOff()) {
                    byReplyUser.setByReplyAvatar(this.loginOffAvatar);
                    byReplyUser.setByReplyNickName("此用户已注销");
                }
            }
            vo.setCreateTimeFormat(TimeFormat.timeCheck(vo.getCreateTime().getTime(), new Date().getTime()));
            if (!tokenStatus){
                LambdaQueryWrapper<BlogGossipCommentLike> lambda = new QueryWrapper<BlogGossipCommentLike>().lambda();
                lambda.eq(BlogGossipCommentLike::getCommentId, vo.getCommentId()).eq(BlogGossipCommentLike::getUid, uid);
                vo.setLike(this.blogGossipCommentLikeService.getOne(lambda) != null);
            }
        }
        return JsonResult.OK(new PageUtil(new PageInfo<>(list)));
    }

    @Override
    public JsonResult createComment(Long gossipId, String content, Long replyCommentId, Long replyUserId, HttpServletRequest request) {
        if(content == null || content.isEmpty() || content.equals("<p><br></p>")) return JsonResult.ERROR(0,"不能提交空白哦 ˋ( ° ▽、° )");
        //获取被回复的碎语
        BlogGossip customaryGossip = this.blogGossipService.getOne(new QueryWrapper<BlogGossip>().lambda().eq(BlogGossip::getId, gossipId));
        if(customaryGossip == null) return JsonResult.ERROR(404, "碎语不存在或者被删除");
        //查询被回复的评论是否存在
        if (replyCommentId != null && replyUserId != null) {
            if (this.getOne(new QueryWrapper<BlogGossipComment>()
                    .lambda()
                    .eq(BlogGossipComment::getGossipId, gossipId)
                    .eq(BlogGossipComment::getCommentId, replyCommentId)
                    .eq(BlogGossipComment::getReplyUser, replyUserId)) == null) return JsonResult.ERROR(404, "被回复的评论不存在");
        }

        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        if (replyUserId != null && replyUserId.equals(user.getId())) return JsonResult.ERROR(400,"不能回复你自己哦");
        if (this.blogUserService.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, user.getId())) == null) return JsonResult.ERROR(404, "此用户不存在！");
        BlogGossipComment blogGossipComment = new BlogGossipComment();
        blogGossipComment.setGossipId(gossipId);
        blogGossipComment.setReplyUser(user.getId());
        blogGossipComment.setByReplyCommentId(replyCommentId);
        blogGossipComment.setCreateTime(new Date());
        blogGossipComment.setContent(content);
        blogGossipComment.setByReplyUser(replyUserId);
        this.save(blogGossipComment);

        if (!customaryGossip.getAuthor().equals(user.getId())) {
            this.eventPublisher.publishEvent(new UserByReplyEvent(
                    this,
                    gossipId,
                    ReplyType.Gossip,
                    replyCommentId != null || replyUserId != null ? replyUserId:customaryGossip.getAuthor(),
                    request.getRequestURL().toString().replaceAll(request.getRequestURI(),""),
                    user.getId(),
                    NoticeSetting.GossipCommentReply,
                    blogGossipComment.getCommentId()
            ));
        } else if (replyUserId != null){
            this.eventPublisher.publishEvent(new UserByReplyEvent(
                    this,
                    gossipId,
                    ReplyType.Gossip,
                    replyUserId,
                    request.getRequestURL().toString().replaceAll(request.getRequestURI(),""),
                    user.getId(),
                    NoticeSetting.GossipCommentReply,
                    blogGossipComment.getCommentId()
            ));
        }


        return JsonResult.OK("评论成功 (￣y▽,￣)╭ ");
    }

    @Override
    public JsonResult commentLike(Long gossipId, Long commentId, HttpServletRequest request) {
        BlogGossipComment one = this.getOne(new QueryWrapper<BlogGossipComment>().lambda().eq(BlogGossipComment::getGossipId, gossipId).eq(BlogGossipComment::getCommentId, commentId));
        if(one == null) return JsonResult.ERROR(404, "服务器没有找到相应碎语评论");
        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        Map<String, Object> likeStatus = new HashMap<>();
        if (this.blogGossipCommentLikeService.remove(new QueryWrapper<BlogGossipCommentLike>().lambda().eq(BlogGossipCommentLike::getCommentId, commentId).eq(BlogGossipCommentLike::getUid, user.getId()))) {
            likeStatus.put("status", false);
            likeStatus.put("likes", this.blogGossipCommentLikeService.count(new QueryWrapper<BlogGossipCommentLike>().lambda().eq(BlogGossipCommentLike::getCommentId, commentId)));
            return JsonResult.OK("取消喜欢了咯 (´。＿。｀)", likeStatus);
        } else {
            BlogGossipCommentLike commentLike = new BlogGossipCommentLike();
            commentLike.setCommentId(commentId);
            commentLike.setUid(user.getId());
            this.blogGossipCommentLikeService.save(commentLike);
            likeStatus.put("status", true);
            likeStatus.put("likes", this.blogGossipCommentLikeService.count(new QueryWrapper<BlogGossipCommentLike>().lambda().eq(BlogGossipCommentLike::getCommentId, commentId)));
            return JsonResult.OK("喜欢成功，感谢你的点赞 (○｀ 3′○)", likeStatus);
        }
    }

    @Override
    public JsonResult deleteGossipCommentById(Long gossipId, Long commentId, HttpServletRequest request) {
        if(this.baseMapper.deleteCommentById(gossipId, commentId, this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId())) {
            return JsonResult.OK("删除成功！");
        } else {
            return JsonResult.ERROR(400, "删除失败，服务器没有找到对应的资源！");
        }
    }
}
