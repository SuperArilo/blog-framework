package com.tty.blog.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PushNoticeEvent extends ApplicationEvent {

    private final Long uid;

    public PushNoticeEvent(Object source, Long uid) {
        super(source);
        this.uid = uid;
    }

}

