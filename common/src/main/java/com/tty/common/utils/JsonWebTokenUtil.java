package com.tty.common.utils;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JWT令牌工具类
 * @version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JsonWebTokenUtil {

    /**
     * 登录令牌负载 key
     */
    private final static String LOGIN_TOKEN_PAYLOAD_KEY = "payload_key";

    /**
     * 令牌密钥集合
     */
    private Map<String, String> secretMap;

    /**
     * 创建一个JWT令牌。
     *
     * @param obj 用于放在JWT负载中的对象，通常为用户身份验证相关的信息。
     * @param expirationTime 令牌的过期时间。
     * @param jwtType JWT令牌的类型，根据类型决定使用的签名算法等。
     * @return 生成的JWT令牌字符串。
     */
    public <T> String createToken(T obj, Date expirationTime, JsonWebTokenTypeEnum jwtType) {
        // 初始化JWT头部信息
        Map<String, Object> header = new HashMap<>();
        // 初始化JWT负载信息
        Map<String, Object> payload = new HashMap<>();

        // 将对象信息转换为JSON字符串，并放入JWT负载中
        payload.put(LOGIN_TOKEN_PAYLOAD_KEY, JSONObject.toJSONString(obj));

        // 根据JWT类型获取对应的密钥，如果不存在则生成新的密钥并保存
        String secret = secretMap.get(jwtType.getType());

        // 创建并签署JWT令牌，使用指定的算法和密钥
        return JWT.create()
                .withHeader(header)
                .withPayload(payload)
                .withExpiresAt(expirationTime)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证JWT令牌的有效性。
     * 该方法使用JWT库，通过指定的算法和秘钥对传入的JWT令牌进行验证，以确定其有效性。
     * 如果令牌验证成功，则方法执行无异常返回；如果令牌无效或格式不正确，则会抛出异常。
     *
     * @param token 待验证的JWT令牌字符串。该字符串是由JWT库根据特定算法和秘钥生成的。
     * @param jwtType JWT令牌的类型，决定使用哪种算法和秘钥进行验证。
     * @throws JWTVerificationException 如果令牌验证失败，即令牌无效或格式不正确，抛出此异常。
     *                                  这个异常封装了JWT验证过程中可能出现的各种错误，例如签名验证失败、
     *                                  算法不匹配、令牌过期等。
     * @throws JWTDecodeException       解码JWT时发生异常，可能是因为JWT格式错误或者无法解析。
     * @throws SignatureVerificationException 验证JWT签名失败，表明令牌可能被篡改。
     * @throws AlgorithmMismatchException JWT签名算法与预期不匹配，可能是因为令牌生成时使用了不同的算法。
     * @throws InvalidClaimException JWT中的声明(claims)无效，可能包含了错误或者过期的信息。
     * @throws TokenExpiredException JWT令牌已过期，无法继续使用。
     */
    public void verifyToken(String token, JsonWebTokenTypeEnum jwtType) throws
            JWTDecodeException,
            SignatureVerificationException,
            AlgorithmMismatchException,
            InvalidClaimException,
            TokenExpiredException {
        // 使用JWT库，指定算法和秘钥对token进行验证
        JWT.require(Algorithm.HMAC256(secretMap.get(jwtType.getType())))
                .build()
                .verify(token);
    }

    /**
     * 从JWT中获取负载数据。
     *
     * @param token 用户的JWT令牌。这是用于识别用户身份的一段加密字符串。
     * @param clazz 需要解析成的类型，指定返回值的类型。根据这个参数，方法会将负载数据解析成相应类型的对象。
     * @return 解析后的负载数据，其类型为clazz参数指定的类型。这个返回值是根据JWT中的特定字段解析出来的。
     */
    public <T> T getPayLoad(String token, Class<T> clazz) {
        // 解码JWT令牌，并提取指定的负载数据
        String objString = JWT.decode(token).getClaim(LOGIN_TOKEN_PAYLOAD_KEY).as(String.class);
        // 将提取的字符串负载数据解析成指定类型T的对象
        return JSONObject.parseObject(objString, clazz);
    }
}
