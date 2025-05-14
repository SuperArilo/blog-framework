package com.tty.common.entity;

import com.tty.common.enums.SocketMessageType;
import lombok.Data;

import java.util.Date;

@Data
public class BaseMessage {
    private SocketMessageType type;
    private String message;
    private Date time;
}
