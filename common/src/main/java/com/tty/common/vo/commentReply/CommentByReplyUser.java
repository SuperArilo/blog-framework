package com.tty.common.vo.commentReply;

import lombok.Data;

@Data
public class CommentByReplyUser {

    private Long byReplyUserId;
    private String byReplyNickName;
    private String byReplyAvatar;
    private Long byReplyCommentId;
}
