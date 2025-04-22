package com.tty.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BlogFriendVO {
    private Long id;
    private String friendName;
    private String friendLocation;
    private String friendIntroduction;
    private String friendAvatar;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    private Date createTime;
    private Long friendUid;
}
