package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.mapper.BlogUserMeansMapper;
import com.tty.blog.service.BlogUserMeansService;
import com.tty.common.entity.BlogUserMeans;
import org.springframework.stereotype.Service;

@Service
public class BlogUserMeansServiceImpl extends ServiceImpl<BlogUserMeansMapper, BlogUserMeans> implements BlogUserMeansService {
}
