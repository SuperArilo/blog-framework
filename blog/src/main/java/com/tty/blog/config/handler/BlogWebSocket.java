package com.tty.blog.config.handler;

import com.alibaba.fastjson2.JSONObject;
import com.tty.blog.mapper.BlogNoticeMapper;
import com.tty.blog.vo.PushNoticeVO;
import com.tty.common.entity.TokenUser;
import com.tty.common.utils.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlogWebSocket implements WebSocketHandler {
    private static final Logger logger = Logger.getLogger(BlogWebSocket.class);
    public static final ConcurrentHashMap<Long, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    @Resource
    BlogNoticeMapper blogNoticeMapper;
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException {
        List<String> s = session.getHandshakeHeaders().get("Sec-Websocket-Protocol");

        TokenUser payLoad = this.jsonWebTokenUtil.getPayLoad(s.get(0), TokenUser.class);

        SESSION_POOL.put(payLoad.getId(), session);
        List<PushNoticeVO> l = blogNoticeMapper.queryNoticeCountByUid(payLoad.getId());
        session.sendMessage(new TextMessage(JSONObject.toJSONString(l)));
        logger.info(payLoad.getId());
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {

    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        List<String> s = session.getHandshakeHeaders().get("Sec-Websocket-Protocol");
        TokenUser payLoad = this.jsonWebTokenUtil.getPayLoad(s.get(0), TokenUser.class);

        SESSION_POOL.remove(payLoad.getId());
        logger.info("断开连接：" + payLoad.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
