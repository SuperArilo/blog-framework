package com.tty.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tty.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class SysBlogArticleVo {
    private Long articleId;
    private String author;
    @JsonFormat(timezone = "GMT+8", pattern = DateUtil.YYYY_MM_DD_PATTERN)
    private Date createTime;
    private String articleTitle;
    private String articleIntroduction;
    private int articleViews;
    private int articleLikes;
    private Integer articleCommentNum;
}
