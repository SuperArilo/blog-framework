package com.tty.blog.event;

import com.tty.blog.enums.LikeType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserLikeGossip extends ApplicationEvent {
    private final Long gossipId;
    private final Long userId;
    private final Long targetUid;
    private final LikeType likeType;
    private final String httpString;

    public UserLikeGossip(Object source, Long gossipId, Long userId, Long targetUid, LikeType likeType, String httpString) {
        super(source);
        this.gossipId = gossipId;
        this.userId = userId;
        this.targetUid = targetUid;
        this.likeType = likeType;
        this.httpString = httpString;
    }
}
