package com.tty.common.vo;

import com.tty.common.vo.commentReply.CommentByReplyUser;
import com.tty.common.vo.commentReply.CommentReplyUser;
import lombok.Data;

import java.util.Date;

@Data
public class BlogCommentVO {
    private Long commentId;
    private Long instanceId;
    private Date createTime;
    private String createTimeFormat;
    private boolean isLike;
    private String content;
    private Integer likes;
    private CommentReplyUser replyUser;
    private CommentByReplyUser byReplyUser;
}
