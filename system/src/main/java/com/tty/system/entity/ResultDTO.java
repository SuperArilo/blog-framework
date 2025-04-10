package com.tty.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class ResultDTO {
    private List<String> sqlKey;
    private List<String> interceptKeys;

}
