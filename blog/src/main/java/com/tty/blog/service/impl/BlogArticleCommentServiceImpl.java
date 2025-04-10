package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogArticle;
import com.tty.blog.entity.BlogArticleComment;
import com.tty.blog.entity.BlogArticleCommentLike;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.mapper.BlogArticleCommentMapper;
import com.tty.blog.service.BlogArticleCommentLikeService;
import com.tty.blog.service.BlogArticleCommentService;
import com.tty.blog.service.BlogArticleService;
import com.tty.blog.service.BlogUserService;
import com.tty.blog.vo.BlogCommentVO;
import com.tty.blog.vo.commentReply.CommentByReplyUser;
import com.tty.blog.vo.commentReply.CommentReplyUser;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.JsonWebTokenUtil;
import com.tty.common.utils.PageUtil;
import com.tty.common.utils.TimeFormat;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogArticleCommentServiceImpl extends ServiceImpl<BlogArticleCommentMapper, BlogArticleComment> implements BlogArticleCommentService {

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    BlogUserService blogUserService;
    @Resource
    BlogArticleService blogArticleService;
    @Resource
    BlogArticleCommentLikeService blogArticleCommentLikeService;

    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;

    @Override
    public JsonResult articleCommentListGet(PageUtil pageUtil, Long articleId, HttpServletRequest request) {
        String token = request.getHeader("token");
        List<BlogCommentVO> commentList = this.baseMapper.getArticleCommentList(new Page<>(pageUtil.getCurrent(), pageUtil.getSize()), articleId).getRecords();

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
        for (BlogCommentVO vo : commentList) {
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
                LambdaQueryWrapper<BlogArticleCommentLike> lambda = new QueryWrapper<BlogArticleCommentLike>().lambda();
                lambda.eq(BlogArticleCommentLike::getCommentId, vo.getCommentId()).eq(BlogArticleCommentLike::getUid, uid);
                vo.setLike(this.blogArticleCommentLikeService.getOne(lambda) != null);
            }
        }
        return JsonResult.OK(new PageUtil(new PageInfo<>(commentList)));
    }

    @Override
    public JsonResult articleCommentAdd(Long articleId, String content, Long replyCommentId, Long replyUserId, HttpServletRequest request) {
        if(content == null || content.isEmpty() || content.equals("<p><br></p>")) return JsonResult.ERROR(0,"不能提交空白哦 ˋ( ° ▽、° )");
        //查询文章是否存在
        if(this.blogArticleService.getOne(new QueryWrapper<BlogArticle>().lambda().eq(BlogArticle::getArticleId, articleId)) == null) return JsonResult.ERROR(404, "文章不存在或者已被删除！");
        //查询被回复的评论是否存在
        if (replyUserId != null && replyCommentId != null) {
            if (this.getOne(
                    new QueryWrapper<BlogArticleComment>().lambda()
                            .eq(BlogArticleComment::getArticleId, articleId)
                            .eq(BlogArticleComment::getCommentId, replyCommentId)
                            .eq(BlogArticleComment::getReplyUser, replyUserId)) == null)
                return JsonResult.ERROR(400, "被回复的评论不存在");
        }

        TokenUser user = jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        //检查是否是回复自己
        if (replyUserId != null && replyUserId.equals(user.getId())) return JsonResult.ERROR(0, "不能回复你自己哦");
        //检查被回复的用户是否存在
        if (this.blogUserService.getOne(
                new QueryWrapper<BlogUser>().lambda()
                        .eq(BlogUser::getUid, user.getId())) == null)
            return JsonResult.ERROR(404, "此用户不存在！");

        //新建评论
        BlogArticleComment comment = new BlogArticleComment();
        comment.setArticleId(articleId);
        comment.setReplyUser(user.getId());
        comment.setByReplyCommentId(replyCommentId);
        comment.setCreateTime(new Date());
        comment.setContent(content);
        comment.setByReplyUser(replyUserId);
        this.save(comment);

//        if (replyUserId != null && replyCommentId != null) {
//            //发布通知事件到被回复的用户
//        }

        return JsonResult.OK("评论成功 (￣y▽,￣)╭ ");
    }

    @Override
    public JsonResult deleteArticleCommentById(Long articleId, Long commentId, HttpServletRequest request, HttpServletResponse response) {
        if (this.baseMapper.deleteArticleCommentById(
                articleId,
                commentId,
                this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId())) {
            return JsonResult.OK("删除成功！");
        } else {
            return JsonResult.ERROR(404, "删除失败，服务器没有找到对应的资源！");
        }
    }

    @Override
    public JsonResult commentLike(Long commentId, Long articleId, HttpServletRequest request) {
        //查询评论是否存在
        if (this.getOne(new QueryWrapper<BlogArticleComment>().lambda()
                        .eq(BlogArticleComment::getArticleId, articleId)
                        .eq(BlogArticleComment::getCommentId, commentId)) == null)
            return JsonResult.ERROR(404, "被回复的评论不存在");
        TokenUser payLoad = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        Map<String, Object> likeStatus = new HashMap<>();
        //判断是否已经点过喜欢
        BlogArticleCommentLike one = this.blogArticleCommentLikeService.getOne(new QueryWrapper<BlogArticleCommentLike>().lambda()
                .eq(BlogArticleCommentLike::getCommentId, commentId)
                .eq(BlogArticleCommentLike::getUid, payLoad.getId()));
        if (one != null) {
            //取消喜欢
            if (this.blogArticleCommentLikeService.remove(
                    new QueryWrapper<BlogArticleCommentLike>().lambda()
                            .eq(BlogArticleCommentLike::getCommentId, commentId)
                            .eq(BlogArticleCommentLike::getUid, payLoad.getId()))) {
                likeStatus.put("status", false);
                likeStatus.put("likes", this.blogArticleCommentLikeService.count(new QueryWrapper<BlogArticleCommentLike>().lambda().eq(BlogArticleCommentLike::getCommentId, commentId)));
                return JsonResult.OK("取消喜欢了咯 (´。＿。｀)", likeStatus);
            } else {
                return JsonResult.ERROR(404, "没有找到记录");
            }
        } else {
            BlogArticleCommentLike like = new BlogArticleCommentLike();
            like.setCommentId(commentId);
            like.setUid(payLoad.getId());
            this.blogArticleCommentLikeService.save(like);
            likeStatus.put("status", true);
            likeStatus.put("likes", this.blogArticleCommentLikeService.count(new QueryWrapper<BlogArticleCommentLike>().lambda().eq(BlogArticleCommentLike::getCommentId, commentId)));
            return JsonResult.OK("喜欢成功，感谢你的点赞 (○｀ 3′○)", likeStatus);
        }
    }
}
