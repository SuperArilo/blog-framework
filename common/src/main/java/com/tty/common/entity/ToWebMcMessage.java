package com.tty.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToWebMcMessage extends BaseMessage {
    private String name;
    private String uuid;
    private String worldName;
}
