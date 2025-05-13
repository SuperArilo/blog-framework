package com.tty.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.enums.SocketMessageType;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BaseMessage {
    private SocketMessageType type;
    private String message;
    @JsonFormat(timezone = DateUtil.TIMEZONE_GMT_8, pattern = DateUtil.YYYY_MM_DD_HH_MM_SS_PATTERN)
    private Date time;
}
