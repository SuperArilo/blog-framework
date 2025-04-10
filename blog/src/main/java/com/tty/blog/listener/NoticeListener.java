package com.tty.blog.listener;

import com.alibaba.fastjson2.JSONObject;
import com.tty.blog.config.handler.BlogWebSocket;
import com.tty.blog.event.PushNoticeEvent;
import com.tty.blog.mapper.BlogNoticeMapper;
import com.tty.blog.vo.PushNoticeVO;
import jakarta.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Component
public class NoticeListener {
    private static final Logger logger = Logger.getLogger(NoticeListener.class);
    @Resource
    BlogNoticeMapper blogNoticeMapper;

    @EventListener(classes = PushNoticeEvent.class)
    public void pushNoticeToUser(PushNoticeEvent event) {
        Long uid = event.getUid();
        List<PushNoticeVO> l = blogNoticeMapper.queryNoticeCountByUid(uid);
        WebSocketSession session = BlogWebSocket.SESSION_POOL.get(uid);
        TextMessage t = new TextMessage(JSONObject.toJSONString(l));
        if (session == null) return;
        try {
            session.sendMessage(t);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }
}
