package com.tty.blog.dto.event;

import lombok.Data;

@Data
public class LikeToUser {

    private Long uid;
    private Long targetUid;

}
