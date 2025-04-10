package com.tty.system.mapper;

import com.tty.system.vo.NoticeTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysNoticeTemplateMapper {
    NoticeTemplateVO getTemplateById(@Param("id") Long id);
}
