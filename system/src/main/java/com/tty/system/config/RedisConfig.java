package com.tty.system.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis配置类
 */
@Configuration
public class RedisConfig {

    /**
     * 创建并配置RedisTemplate，用于操作Redis数据库。
     * 这个配置使用Jackson2JsonRedisSerializer来序列化和反序列化Redis的值（value），
     * 以及使用StringRedisSerializer来序列化和反序列化Redis的键（key）和哈希键（hash key）。
     * 它使得存储在Redis中的对象能够被正确地转换和读取。
     *
     * @param redisConnectionFactory Redis连接工厂，用于创建与Redis服务器的连接。
     * @return 配置好的RedisTemplate实例，可用于执行Redis操作。
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 配置ObjectMapper以支持JSON序列化和反序列化，以及类型信息的自动检测。
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,ObjectMapper.DefaultTyping.NON_FINAL);

        // 使用Jackson ObjectMapper创建JSON序列化器。
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // 配置RedisTemplate的序列化器，以及连接工厂。
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }

}
