package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.blog.entity.BlogArticleCommentLike;
import com.tty.blog.mapper.BlogArticleCommentLikeMapper;
import com.tty.blog.service.BlogArticleCommentLikeService;
import org.springframework.stereotype.Service;

@Service
public class BlogArticleCommentLikesServiceImpl extends ServiceImpl<BlogArticleCommentLikeMapper, BlogArticleCommentLike> implements BlogArticleCommentLikeService {
}
