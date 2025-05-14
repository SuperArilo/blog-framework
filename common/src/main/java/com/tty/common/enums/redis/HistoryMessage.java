package com.tty.common.enums.redis;

import lombok.Getter;

@Getter
public enum HistoryMessage {
    OnlineTalk("OnlineTalk"),
    History("history");

    private final String key;

    HistoryMessage(String key) {
        this.key = key;
    }
}
