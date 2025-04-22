package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogNotice;
import com.tty.common.vo.BlogNoticeContentVO;
import com.tty.common.vo.PushNoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogNoticeMapper extends BaseMapper<BlogNotice> {
    List<BlogNoticeContentVO> queryNoticeByUid(@Param("classType") Integer classType,
                                               @Param("uid") Long uid);
    Integer queryNoticeIsHave(@Param("noticeIds") List<Long> noticeIds);
    Integer readNotice(@Param("noticeIds")List<Long> noticeIds,
                       @Param("uid") Long uid);

    List<PushNoticeVO> queryNoticeCountByUid(@Param("uid") Long uid);
}
