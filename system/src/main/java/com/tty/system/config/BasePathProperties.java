package com.tty.system.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BasePathProperties {
    @Value("${context-path}")
    public String basePath;
}
