package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.blog.entity.BlogGossipCommentLike;
import com.tty.blog.mapper.BlogGossipCommentLikeMapper;
import com.tty.blog.service.BlogGossipCommentLikeService;
import org.springframework.stereotype.Service;

@Service
public class BlogGossipCommentLikeServiceImpl extends ServiceImpl<BlogGossipCommentLikeMapper, BlogGossipCommentLike> implements BlogGossipCommentLikeService {
}
