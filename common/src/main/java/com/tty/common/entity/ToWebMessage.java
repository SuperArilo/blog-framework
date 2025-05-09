package com.tty.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToWebMessage extends BaseMessage {
    private String playerName;
    private String uuid;
    private String worldName;
}
