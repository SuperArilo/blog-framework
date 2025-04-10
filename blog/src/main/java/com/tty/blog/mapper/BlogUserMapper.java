package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.vo.BlogUserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogUserMapper extends BaseMapper<BlogUser> {
    BlogUserInfoVO loginUser(@Param("email") String email,
                             @Param("password") String password);
    BlogUserInfoVO queryByUid(@Param("uid") Long uid);
}
