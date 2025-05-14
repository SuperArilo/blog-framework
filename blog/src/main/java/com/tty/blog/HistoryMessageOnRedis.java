package com.tty.blog;

import com.tty.blog.config.handler.OnlineTalkSocketHandler;
import com.tty.common.entity.ToWebMessage;
import com.tty.common.enums.redis.HistoryMessage;
import com.tty.common.utils.FunctionTool;
import com.tty.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class HistoryMessageOnRedis implements ApplicationRunner {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private FunctionTool functionTool;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<ToWebMessage> o = (List<ToWebMessage>) this.redisUtil.get(this.functionTool.buildRedisKey(false, HistoryMessage.OnlineTalk, HistoryMessage.History));
        if (o == null) {
            log.debug("聊天历史不存在");
            return;
        }
        OnlineTalkSocketHandler.HISTORY_MESSAGES = o;
        log.info("成功加载 {} 条聊天历史", o.size());
    }

    @EventListener(ContextClosedEvent.class)
    public void onClose() {
        log.info("保存聊天历史");
        this.redisUtil.set(this.functionTool.buildRedisKey(false, HistoryMessage.OnlineTalk, HistoryMessage.History), OnlineTalkSocketHandler.HISTORY_MESSAGES);
    }
}
