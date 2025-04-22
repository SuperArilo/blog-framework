package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogCiallo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogCialloMapper extends BaseMapper<BlogCiallo> {
    List<BlogCiallo> getLists();
}
