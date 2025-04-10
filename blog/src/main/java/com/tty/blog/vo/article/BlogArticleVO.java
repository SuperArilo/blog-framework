package com.tty.blog.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BlogArticleVO {

    private Long id;
    private Long publisher;
    private Boolean like;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    private Date createTime;
    private String articleTitle;
    private String articleIntroduction;
    private Integer articleLikes;
    private Integer articleViews;
    private Integer comments;
    private String articlePicture;
}
