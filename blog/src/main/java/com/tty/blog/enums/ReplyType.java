package com.tty.blog.enums;

import lombok.Getter;

@Getter
public enum ReplyType {
    Detail("detail"),
    Gossip("gossip");

    private final String type;

    ReplyType(String type) {
        this.type = type;
    }

}
