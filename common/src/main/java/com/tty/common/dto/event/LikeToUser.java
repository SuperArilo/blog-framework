package com.tty.common.dto.event;

import lombok.Data;

@Data
public class LikeToUser {

    private Long uid;
    private Long targetUid;

}
