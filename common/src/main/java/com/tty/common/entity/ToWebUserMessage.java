package com.tty.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToWebUserMessage extends BaseMessage {
    private Long uid;
    private String name;
}
