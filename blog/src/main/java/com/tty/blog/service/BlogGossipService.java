package com.tty.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.blog.entity.BlogGossip;
import com.tty.blog.vo.gossip.GossipVO;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogGossipService extends IService<BlogGossip> {
    GossipVO gossipListGet(PageUtil pageUtil,
                           Long viewUid,
                           Long targetId,
                           Long commentId,
                           HttpServletRequest request);
    JsonResult gossipLike(Long gossipId,
                          HttpServletRequest request);
    JsonResult gossipCreateByUser(String content,
                                  HttpServletRequest request);
    JsonResult gossipDeleteByUser(Long gossipId, HttpServletRequest request);
}
