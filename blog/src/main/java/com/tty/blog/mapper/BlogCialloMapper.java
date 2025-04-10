package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogCiallo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogCialloMapper extends BaseMapper<BlogCiallo> {
    List<BlogCiallo> getLists();
}
