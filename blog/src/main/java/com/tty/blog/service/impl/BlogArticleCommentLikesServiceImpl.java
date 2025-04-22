package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.blog.service.BlogArticleCommentLikeService;
import com.tty.common.entity.BlogArticleCommentLike;
import com.tty.common.mapper.BlogArticleCommentLikeMapper;
import org.springframework.stereotype.Service;

@Service
public class BlogArticleCommentLikesServiceImpl extends ServiceImpl<BlogArticleCommentLikeMapper, BlogArticleCommentLike> implements BlogArticleCommentLikeService {
}
