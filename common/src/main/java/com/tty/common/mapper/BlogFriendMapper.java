package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogFriend;
import com.tty.common.vo.BlogFriendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface BlogFriendMapper extends BaseMapper<BlogFriend> {
    List<BlogFriendVO> getLists();
    Boolean queryUserHadApply(@Param("uid") Long uid);
    Integer insertApply(@Param("name") String name,
                        @Param("location") String location,
                        @Param("introduction") String introduction,
                        @Param("avatar") String avatar,
                        @Param("time") Date time,
                        @Param("uid") Long uid);
}
