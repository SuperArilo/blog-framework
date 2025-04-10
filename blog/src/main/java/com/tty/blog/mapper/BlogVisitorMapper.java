package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogVisitor;
import com.tty.blog.vo.BlogVisitorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogVisitorMapper extends BaseMapper<BlogVisitor> {
    List<BlogVisitorVO> queryVisitorList();
}
