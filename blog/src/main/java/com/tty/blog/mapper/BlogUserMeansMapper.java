package com.tty.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.blog.dto.sql.UserProfilesModifyDTO;
import com.tty.blog.entity.BlogUserMeans;
import com.tty.blog.vo.BlogUserProfilesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogUserMeansMapper extends BaseMapper<BlogUserMeans> {
    BlogUserProfilesVO queryUserProfiles(@Param("uid") Long uid);
    Boolean queryViewerIsLikeToTarget(@Param("viewer") Long viewer,
                                      @Param("targetUid") Long targetUid);
    boolean updateUserProfiles(UserProfilesModifyDTO dto);
}
