package com.tty.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.tty.system.entity.SysUserEntity;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

}
