package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogGuestbook;
import com.tty.common.vo.BlogGuestbookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogGuestbookMapper extends BaseMapper<BlogGuestbook> {
    List<BlogGuestbookVO> guestbookList();
}
