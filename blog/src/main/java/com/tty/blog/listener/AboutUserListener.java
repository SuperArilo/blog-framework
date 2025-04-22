package com.tty.blog.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tty.blog.config.handler.BlogWebSocket;
import com.tty.common.dto.event.LikeToUser;
import com.tty.blog.event.UserByReplyEvent;
import com.tty.blog.event.UserLikeGossip;
import com.tty.blog.event.UserLikeTargetUser;
import com.tty.blog.event.UserLoginOutEvent;
import com.tty.common.enums.NoticeSetting;
import com.tty.common.enums.SQLTemplateType;
import com.tty.common.enums.redis.TokenExpiredKey;
import com.tty.common.enums.redis.UserKey;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.RedisUtil;
import com.tty.system.entity.ResultDTO;
import com.tty.system.entity.SysNotice;
import com.tty.system.entity.SysNoticeSetting;
import com.tty.system.mapper.SysNoticeMapper;
import com.tty.system.mapper.SysNoticeSettingMapper;
import com.tty.system.mapper.SysNoticeTemplateMapper;
import com.tty.system.utils.TemplateUtil;
import com.tty.system.vo.NoticeTemplateVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AboutUserListener {

    private static final String Title = "啊哦(⊙o⊙)？";
    private static final String Content = "这是一条未知错误的通知，请删除掉";
    @Resource
    RedisUtil redisUtil;
    @Resource
    SysNoticeTemplateMapper sysNoticeTemplateMapper;
    @Resource
    SysNoticeSettingMapper sysNoticeSettingMapper;
    @Resource
    TemplateUtil templateUtil;
    @Resource
    SysNoticeMapper sysNoticeMapper;
    @Resource
    FunctionTool functionTool;
    @Value("${jwt.token.seconds.login}")
    private Long Expire_Time;

    @Async
    @EventListener(UserByReplyEvent.class)
    public void replyEvent(UserByReplyEvent event) {
        NoticeTemplateVO templateVO = this.sysNoticeTemplateMapper.getTemplateById(
                this.sysNoticeSettingMapper.selectOne(
                        new QueryWrapper<SysNoticeSetting>().lambda()
                                .eq(SysNoticeSetting::getTemplateType, event.getTemplate().getType())).getSettingTemplateId());
        SysNotice sysNotice = new SysNotice();
        if (templateVO != null) {
            //模板字符串key
            String templateContent = templateVO.getContent();
            //将key分离处理为对应类型的key
            ResultDTO dto = this.templateUtil.detachTemplateString(this.templateUtil.extractStringsInBraces(templateContent), SQLTemplateType.class);

            //根据sqlKey查询然后修改模板内容
            templateContent = this.templateUtil.replaceSqlKey(this.templateUtil.getParaphraseKeys(dto.getSqlKey(), event.getReplyUid()), templateContent);

            //链接地址替换
            templateContent = this.commentReplyLinkReplace(dto.getInterceptKeys(), templateContent, event);

            sysNotice.setTitle(templateVO.getTitle());
            sysNotice.setContent(templateContent);
        } else {
            sysNotice.setTitle(Title);
            sysNotice.setContent(Content);
        }
        sysNotice.setCreateTime(new Date());
        sysNotice.setClassType(2);
        sysNotice.setAuthorUid(0L);
        sysNotice.setTargetUid(event.getUid());
        this.sysNoticeMapper.insert(sysNotice);
    }
    @Async
    @EventListener(classes = UserLoginOutEvent.class)
    public void userLoginOut(UserLoginOutEvent event) {
        BlogWebSocket.SESSION_POOL.remove(event.getUid());
        this.redisUtil.delete(this.functionTool.buildRedisKey(false, UserKey.User.getKey(), event.getUid()));
        this.redisUtil.set(this.functionTool.buildRedisKey(false, TokenExpiredKey.Main.getKey(), event.getUid()), event.getToken(), this.Expire_Time, TimeUnit.MINUTES);
    }
    @Async
    @EventListener(classes = UserLikeTargetUser.class)
    public void userLikeToTargetUser(UserLikeTargetUser event) {
        LikeToUser likeToUser = event.getLikeToUser();
        NoticeTemplateVO templateVO = this.sysNoticeTemplateMapper.getTemplateById(
                this.sysNoticeSettingMapper.selectOne(
                        new QueryWrapper<SysNoticeSetting>().lambda()
                                .eq(SysNoticeSetting::getTemplateType, NoticeSetting.LikeToUser.getType())).getSettingTemplateId());
        SysNotice sysNotice = new SysNotice();
        if (templateVO != null) {
            String templateContent = templateVO.getContent();
            ResultDTO dto = this.templateUtil.detachTemplateString(this.templateUtil.extractStringsInBraces(templateContent), SQLTemplateType.class);
            templateContent = this.templateUtil.replaceSqlKey(this.templateUtil.getParaphraseKeys(dto.getSqlKey(), likeToUser.getUid()), templateContent);
            sysNotice.setTitle(templateVO.getTitle());
            sysNotice.setContent(templateContent);
        } else {
            sysNotice.setTitle(Title);
            sysNotice.setContent(Content);
        }
        sysNotice.setAuthorUid(0L);
        sysNotice.setCreateTime(new Date());
        sysNotice.setClassType(4);
        sysNotice.setTargetUid(likeToUser.getTargetUid());
        this.sysNoticeMapper.insert(sysNotice);
    }

    private String commentReplyLinkReplace(List<String> keys, String content, UserByReplyEvent event) {
        for (String key : keys) {
            switch (key) {
                case "gossipCommentHref" -> content = content.replace("{" + key + "}", event.getHttpString() + "/" + event.getReplyType().getType() + "?" + "targetId=" + event.getTargetId() + "&commentId=" + event.getReplyCommentId());
                case "articleCommentHref" -> content = content.replace("{" + key + "}", event.getHttpString() + "/" + event.getReplyType().getType() + "?" + "threadId=" + event.getTargetId() + "&commentId=" + event.getReplyCommentId());
            }
        }
        return content;
    }

    private String gossipOtherReplace(List<String> keys, String content, UserLikeGossip event) {
        for(String key : keys) {
            if (key.equals("gossipHref")) {
                content = content.replace("{" + key + "}", event.getHttpString() + "/" + event.getLikeType().getType() + "?" + "targetId=" + event.getGossipId());
            }
        }
        return content;
    }
}
