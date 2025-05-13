package com.tty.blog.config.handler;


import com.google.gson.Gson;
import com.tty.common.entity.FromMinecraftMessage;
import com.tty.common.entity.ToMinecraftMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MinecraftServerSocketHandler implements WebSocketHandler {

    public static final ConcurrentHashMap<String, WebSocketSession> MINECRAFT_SERVER_SESSION_POOL = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException {
        String serverName = this.getServerName(session);
        if (serverName == null) return;
        MINECRAFT_SERVER_SESSION_POOL.put(serverName, session);
        log.info("minecraft server: {} connected!", serverName);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        String serverName = this.getServerName(session);
        if (serverName == null) return;
        FromMinecraftMessage fromMinecraftMessage = new Gson().fromJson((String) message.getPayload(), FromMinecraftMessage.class);
        log.info("minecraft server {} send message: {}", fromMinecraftMessage.getServerName(), fromMinecraftMessage.getMessage());
        OnlineTalkSocketHandler.sendFromMinecraftMessageToUser(fromMinecraftMessage);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws IOException {
        String serverName = this.getServerName(session);
        if (serverName == null) return;
        log.error("transport error", exception);
        MINECRAFT_SERVER_SESSION_POOL.remove(serverName);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws IOException  {
        String serverName = this.getServerName(session);
        if (serverName == null) return;
        MINECRAFT_SERVER_SESSION_POOL.remove(serverName);
        log.info("minecraft server: {} disconnected!", serverName);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getServerName(@NonNull WebSocketSession session) throws IOException {
        URI uri = session.getUri();
        if (uri == null) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return null;
        }
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String serverName = queryParams.getFirst("name");
        if (serverName == null || serverName.isEmpty())  {
            session.close(CloseStatus.POLICY_VIOLATION);
            return null;
        }
        return serverName;
    }

    public static void sendMessage(ToMinecraftMessage message) {
        MINECRAFT_SERVER_SESSION_POOL.forEach((k, v) -> {
            if (!v.isOpen()) return;
            try {
                v.sendMessage(new TextMessage(new Gson().toJson(message)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
