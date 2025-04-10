package com.tty.blog.vo.gossip;

import com.tty.blog.vo.BlogCommentVO;
import com.tty.common.utils.PageUtil;
import lombok.Data;

@Data
public class GossipVO {

    private InstanceVO targetGossip;
    private BlogCommentVO targetComment;
    private PageUtil instance;

}
