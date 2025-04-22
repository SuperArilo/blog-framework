package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.dto.sql.UserProfilesModifyDTO;
import com.tty.common.entity.BlogUserMeans;
import com.tty.common.vo.BlogUserProfilesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogUserMeansMapper extends BaseMapper<BlogUserMeans> {
    BlogUserProfilesVO queryUserProfiles(@Param("uid") Long uid);
    Boolean queryViewerIsLikeToTarget(@Param("viewer") Long viewer,
                                      @Param("targetUid") Long targetUid);
    boolean updateUserProfiles(UserProfilesModifyDTO dto);
}
