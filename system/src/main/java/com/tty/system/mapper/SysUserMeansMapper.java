package com.tty.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.system.entity.SysUserMeans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMeansMapper extends BaseMapper<SysUserMeans> {
    Map<String, Object> dynamicMatching(@Param("listKeys") List<String> listKeys,
                                        @Param("uid") Long uid);
}
