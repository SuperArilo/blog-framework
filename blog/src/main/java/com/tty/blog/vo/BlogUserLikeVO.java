package com.tty.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

@Data
public class BlogUserLikeVO {

    private Long id;
    private Long uid;
    private String nickName;
    private String avatar;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_BIAS_PATTERN)
    private String createTime;

}
