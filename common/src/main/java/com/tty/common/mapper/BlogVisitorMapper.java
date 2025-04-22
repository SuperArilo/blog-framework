package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogVisitor;
import com.tty.common.vo.BlogVisitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogVisitorMapper extends BaseMapper<BlogVisitor> {
    List<BlogVisitorVO> queryVisitorList();
    void upsert(@Param("instance") BlogVisitor blogVisitor);
}
