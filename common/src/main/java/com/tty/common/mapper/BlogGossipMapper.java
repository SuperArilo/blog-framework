package com.tty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tty.common.entity.BlogGossip;
import com.tty.common.vo.gossip.InstanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogGossipMapper extends BaseMapper<BlogGossip> {
    List<InstanceVO> gossipListGet(@Param("viewUid") Long viewUid);
    InstanceVO queryGossipById(@Param("gossipId") Long gossipId);
    boolean deleteGossipById(@Param("gossipId") Long gossipId, @Param("uid") Long uid);
}
