package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogUser;
import com.tty.common.vo.BlogUserInfoVO;
import com.tty.common.vo.BlogUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogUserMapper extends BaseMapper<BlogUser> {
    BlogUserInfoVO loginUser(@Param("email") String email,
                             @Param("password") String password);
    BlogUserInfoVO queryByUid(@Param("uid") Long uid);
    BlogUserVO queryHaveUser(@Param("email") String email,
                             @Param("uid") Long uid);
}
