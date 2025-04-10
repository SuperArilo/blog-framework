package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.blog.entity.BlogArticle;
import com.tty.blog.entity.BlogArticleLike;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.mapper.BlogArticleMapper;
import com.tty.blog.mapper.BlogArticleLikeMapper;
import com.tty.blog.service.BlogArticleService;
import com.tty.blog.service.BlogUserService;
import com.tty.blog.vo.article.ArticleContentVO;
import com.tty.blog.vo.article.BlogArticleVO;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.enums.redis.ArticleKey;
import com.tty.common.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {

    @Resource
    BlogArticleLikeMapper blogArticleLikeMapper;

    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    FunctionTool functionTool;
    @Resource
    RedisUtil redisUtil;
    @Resource
    BlogUserService blogUserService;
    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;

    @Override
    public JsonResult getBlogArticleList(PageUtil pageUtil, String keyword, HttpServletRequest request) {
        String token = request.getHeader("token");
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());
        List<BlogArticleVO> list = this.baseMapper.selectBlogArticleList(keyword);
        if(StringUtils.hasLength(token)) {
            // 登录处理
            try {
                jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            } catch (Exception ignored) {}
            TokenUser payLoad = jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
            for (BlogArticleVO vo : list) {
                vo.setLike(this.blogArticleLikeMapper.exists(new QueryWrapper<BlogArticleLike>().lambda().eq(BlogArticleLike::getUid, payLoad.getId()).eq(BlogArticleLike::getArticleId, vo.getId())));
            }
        }
        return JsonResult.OK(new PageUtil(new PageInfo<>(list)));


    }

    @Override
    public JsonResult getBlogArticleContent(Long articleId, HttpServletRequest request) {
        TokenUser tokenUser;
        try {
            String token = request.getHeader("token");
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            tokenUser = this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
        } catch (Exception e) {
            tokenUser = null;
        }

        ArticleContentVO vo = this.baseMapper.queryArticle(articleId, tokenUser == null ? null : tokenUser.getId());
        if (vo == null) return JsonResult.ERROR(400, "服务器没有查询到此文章的id");
        BlogUser one = this.blogUserService.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, vo.getPublisher()));
        if (one == null) {
            vo.setPublisher(0L);
            vo.setAvatar(this.loginOffAvatar);
            vo.setNickName("该用户已注销");
        } else if (one.getLoginOff()) {
            vo.setAvatar(this.loginOffAvatar);
            vo.setNickName("该用户已注销");
        }

        //同一个ip固定时间增长一次浏览记录
        String ipAddress = IpUtil.getIpAdder(request);
        String key = this.functionTool.buildRedisKey(false, ArticleKey.Article.getKey(), ArticleKey.Ips.getKey(), ipAddress);
        if(this.redisUtil.get(key) == null) {
            this.baseMapper.increaseArticleView(articleId);
            this.redisUtil.set(key, ipAddress, 12, TimeUnit.HOURS);
        }

        return JsonResult.OK("查询成功", vo);
    }

    @Override
    public JsonResult increaseBlogArticleLike(Long articleId, HttpServletRequest request) {
        if (this.getOne(new QueryWrapper<BlogArticle>().lambda().eq(BlogArticle::getArticleId, articleId)) == null) return JsonResult.ERROR(404, "服务器没有查询到此文章的id");
        TokenUser user = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class);
        Map<String, Object> likeStatus = new HashMap<>();
        if (this.blogArticleLikeMapper.delete(new QueryWrapper<BlogArticleLike>().lambda().eq(BlogArticleLike::getArticleId, articleId).eq(BlogArticleLike::getUid, user.getId())) == 1) {
            likeStatus.put("status", false);
            likeStatus.put("likes", this.blogArticleLikeMapper.queryArticleLikes(articleId));
            return JsonResult.OK("取消喜欢了咯 (´。＿。｀)", likeStatus);
        } else {
            BlogArticleLike blogArticleLike = new BlogArticleLike();
            blogArticleLike.setArticleId(articleId);
            blogArticleLike.setUid(user.getId());
            this.blogArticleLikeMapper.insert(blogArticleLike);
            likeStatus.put("status", true);
            likeStatus.put("likes", this.blogArticleLikeMapper.queryArticleLikes(articleId));
            return JsonResult.OK("喜欢成功，感谢你的点赞 (○｀ 3′○)", likeStatus);
        }
    }
}
