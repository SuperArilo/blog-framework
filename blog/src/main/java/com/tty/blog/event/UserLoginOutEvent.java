package com.tty.blog.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserLoginOutEvent extends ApplicationEvent {

    private final String token;
    private final String ip;
    private final Long uid;

    public UserLoginOutEvent(Object source, String token, String ip, Long uid) {
        super(source);
        this.token = token;
        this.ip = ip;
        this.uid = uid;
    }

}
