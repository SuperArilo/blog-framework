package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.blog.entity.BlogUserMeans;
import com.tty.blog.mapper.BlogUserMeansMapper;
import com.tty.blog.service.BlogUserMeansService;
import org.springframework.stereotype.Service;

@Service
public class BlogUserMeansServiceImpl extends ServiceImpl<BlogUserMeansMapper, BlogUserMeans> implements BlogUserMeansService {
}
