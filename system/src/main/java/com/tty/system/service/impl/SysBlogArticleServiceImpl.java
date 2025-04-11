package com.tty.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.mapper.SysBlogArticleMapper;
import com.tty.system.service.SysBlogArticleService;
import com.tty.system.vo.SysBlogArticleVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SysBlogArticleServiceImpl implements SysBlogArticleService {

    @Resource
    private SysBlogArticleMapper sysBlogArticleMapper;


    @Override
    public JsonResult queryBlogArticle(Integer pageNum,
                                       Integer pageSize, String articleTitle, String author, String beforeTime, String afterTime, String sortName, String sort) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysBlogArticleVo> sysBlogArticleVos = this.sysBlogArticleMapper.aQueryBlogArticle(articleTitle, author, beforeTime, afterTime, sortName, sort);
        PageInfo<SysBlogArticleVo> pageInfo = new PageInfo<>(sysBlogArticleVos);
        return JsonResult.OK("ok", new PageUtil(pageInfo));
    }

    @Override
    public String createBlogArticle(Long uid, String articleTitle, String articleIntroduction, String articleContent) {
        return "";
    }

    @Override
    public JsonResult findAccuratelyArticle(Long articleId) {
        return null;
    }

    @Override
    public JsonResult modifyBlogArticle(Integer id, String content, String title, String introduction, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public JsonResult deleteBlogArticle(List<Long> articleIds) {
        return null;
    }
}
