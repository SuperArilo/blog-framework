package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogGuestbook;
import com.tty.blog.vo.BlogGuestbookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogGuestbookMapper extends BaseMapper<BlogGuestbook> {
    List<BlogGuestbookVO> guestbookList();
}
