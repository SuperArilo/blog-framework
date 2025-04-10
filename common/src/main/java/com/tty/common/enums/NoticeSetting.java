package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum NoticeSetting {

    GossipCommentReply("commentReply"),
    FirstRegister("firstRegister"),
    LikeGossip("likeGossip"),
    LikeToUser("likeToUser"),
    NoTemplate("noTemplate"),
    ArticleCommentReply("articleCommentReply");

    private final String type;

    NoticeSetting(String type) {
        this.type = type;
    }

}
