package com.tty.blog.event;

import com.tty.blog.enums.ReplyType;
import com.tty.common.enums.NoticeSetting;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserByReplyEvent extends ApplicationEvent {

    private final Long targetId;
    private final ReplyType replyType;
    private final Long uid;
    private final Long replyUid;
    private final String httpString;
    private final NoticeSetting template;
    private final Long replyCommentId;


    public UserByReplyEvent(Object source, Long targetId, ReplyType replyType, Long uid, String httpString, Long replyUid, NoticeSetting template, Long replyCommentId) {
        super(source);
        this.targetId = targetId;
        this.replyType = replyType;
        this.uid = uid;
        this.replyUid = replyUid;
        this.httpString = httpString;
        this.template = template;
        this.replyCommentId = replyCommentId;
    }


}
