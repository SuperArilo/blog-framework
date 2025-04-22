package com.tty.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BlogUserProfilesVO {
    private Long uid;
    private String email;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    private Date registerTime;
    private String category;
    private String avatar;
    private String autograph;
    private String nickName;
    private Integer age;
    private Integer sex;
    private String contact;
    private String location;
//    private String background;
    private Integer likeNum;
    private Boolean viewerLike;
}
