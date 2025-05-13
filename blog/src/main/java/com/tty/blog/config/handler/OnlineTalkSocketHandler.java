package com.tty.blog.config.handler;

import com.google.gson.Gson;
import com.tty.common.entity.*;
import com.tty.common.enums.SocketMessageType;
import com.tty.common.utils.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class OnlineTalkSocketHandler implements WebSocketHandler {

    private final Gson gson = new Gson();
    public static final ConcurrentHashMap<Long, WebSocketSession> ONLINE_TALK_SESSION_POOL = new ConcurrentHashMap<>();
    @Resource
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException {
        TokenUser tokenUser = this.getUser(session);
        if (tokenUser == null) return;
        ONLINE_TALK_SESSION_POOL.put(tokenUser.getId(), session);
        log.info("user uid: {} connected", tokenUser.getId());
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        TokenUser user = this.getUser(session);
        if (user == null) return;
        FromWebMessage fromWebMessage;
        try {
            fromWebMessage = this.gson.fromJson((String) message.getPayload(), FromWebMessage.class);
        } catch (Exception e) {
            log.error("message error", e);
            return;
        }
        ToMinecraftMessage toMinecraftMessage = new ToMinecraftMessage();
        toMinecraftMessage.setMessage(fromWebMessage.getMessage());
        toMinecraftMessage.setTime(new Date());
        toMinecraftMessage.setType(fromWebMessage.getType());
        sendMessageToUser(fromWebMessage.getMessage(), session, user);
        MinecraftServerSocketHandler.sendMessage(toMinecraftMessage);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws IOException {
        log.error("transport error", exception);
        TokenUser user = this.getUser(session);
        if (user == null) return;
        ONLINE_TALK_SESSION_POOL.remove(user.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws IOException {
        TokenUser user = this.getUser(session);
        if (user == null) return;
        ONLINE_TALK_SESSION_POOL.remove(user.getId());
        log.info("uid: {} closed", user.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private TokenUser getUser(WebSocketSession session) throws IOException {
        URI uri = session.getUri();
        if (uri == null) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return null;
        }
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String token = queryParams.getFirst("token");
        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return null;
        }
        return this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
    }

    public static void sendFromMinecraftMessageToUser(FromMinecraftMessage message) {
        ONLINE_TALK_SESSION_POOL.forEach((k, v) -> {
            if (!v.isOpen()) return;
            ToWebMcMessage webMessage = new ToWebMcMessage();
            webMessage.setName(message.getPlayerName());
            webMessage.setUuid(message.getUuid());
            webMessage.setTime(message.getTime());
            webMessage.setType(message.getType());
            webMessage.setMessage(message.getMessage());
            webMessage.setWorldName(message.getWorldName());
            try {
                v.sendMessage(new TextMessage(new Gson().toJson(webMessage)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void sendMessageToUser(String message, WebSocketSession session, TokenUser user) {
        ONLINE_TALK_SESSION_POOL.forEach((k, v) -> {
            if (!v.isOpen() || v.equals(session)) return;
            ToWebUserMessage toWebUserMessage = new ToWebUserMessage();
            toWebUserMessage.setMessage(message);
            toWebUserMessage.setType(SocketMessageType.WEB_OTHER);
            toWebUserMessage.setTime(new Date());
            toWebUserMessage.setName(user.getUsername());
            toWebUserMessage.setUid(user.getId());
            try {
                v.sendMessage(new TextMessage(new Gson().toJson(toWebUserMessage)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
