package com.tty.common.vo.gossip;

import com.tty.common.vo.BlogCommentVO;
import com.tty.common.utils.PageUtil;
import lombok.Data;

@Data
public class GossipVO {

    private InstanceVO targetGossip;
    private BlogCommentVO targetComment;
    private PageUtil instance;

}
