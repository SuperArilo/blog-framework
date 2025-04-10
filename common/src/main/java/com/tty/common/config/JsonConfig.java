package com.tty.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

    /**
     * 创建并配置一个ObjectMapper实例，用于进行JSON的序列化和反序列化。
     * 这个方法配置了ObjectMapper以支持将Long和long类型的值序列化为字符串。
     *
     * @return ObjectMapper 配置好的ObjectMapper实例，可用于处理JSON数据。
     */
    @Bean
    public ObjectMapper objectMapper() {
        // 创建一个SimpleModule实例，并向其注册Long和long类型的序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 注册模块并返回配置好的ObjectMapper实例
        return new ObjectMapper().registerModule(simpleModule);
    }

}
