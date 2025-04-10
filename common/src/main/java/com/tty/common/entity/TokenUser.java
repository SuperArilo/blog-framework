package com.tty.common.entity;

import com.tty.common.enums.JsonWebTokenTypeEnum;
import lombok.Data;

@Data
public class TokenUser {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    private JsonWebTokenTypeEnum type;
}
