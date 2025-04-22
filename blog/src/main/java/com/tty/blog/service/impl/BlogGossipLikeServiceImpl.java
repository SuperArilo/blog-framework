package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.mapper.BlogGossipLikeMapper;
import com.tty.blog.service.BlogGossipLikeService;
import com.tty.common.entity.BlogGossipLike;
import org.springframework.stereotype.Service;

@Service
public class BlogGossipLikeServiceImpl extends ServiceImpl<BlogGossipLikeMapper, BlogGossipLike> implements BlogGossipLikeService {
}
