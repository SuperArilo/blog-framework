package com.tty.blog.enums;

import lombok.Getter;

@Getter
public enum LikeType {
    Gossip("gossip");
    private final String type;
    LikeType(String type) {
        this.type = type;
    }

}
