package com.tty.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.system.entity.SysImageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysImageRecordMapper extends BaseMapper<SysImageRecord> {
}
