package com.tty.system.service;

import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysBlogArticleService {
    JsonResult queryBlogArticle(Integer pageNum,
                                Integer pageSize,
                                String articleTitle,
                                String author,
                                String beforeTime,
                                String afterTime,
                                String sortName,
                                String sort);
    String createBlogArticle(Long uid, String articleTitle, String articleIntroduction, String articleContent);
    JsonResult findAccuratelyArticle(Long articleId);
    JsonResult modifyBlogArticle(Integer id,
                                 String content,
                                 String title,
                                 String introduction,
                                 MultipartFile file,
                                 HttpServletRequest request);
    JsonResult deleteBlogArticle(List<Long> articleIds);
}
