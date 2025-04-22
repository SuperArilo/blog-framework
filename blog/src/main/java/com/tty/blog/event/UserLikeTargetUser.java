package com.tty.blog.event;

import com.tty.common.dto.event.LikeToUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserLikeTargetUser extends ApplicationEvent {

    private final LikeToUser likeToUser;

    public UserLikeTargetUser(Object source, LikeToUser likeToUser) {
        super(source);
        this.likeToUser = likeToUser;
    }

}
