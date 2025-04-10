package com.tty.system.utils;

import com.tty.system.entity.ResultDTO;

import java.util.List;
import java.util.Map;

public interface TemplateUtil {
    /**
     *
     * @param keys sql key
     * @param uid 指定查询的用户uid
     * @return 返回基于uid的sql value
     */
    Map<String, Object> getParaphraseKeys(List<String> keys, Long uid);

    /**
     *
     * @param keys 从模板里分离出来的key
     * @param verifyClass 需要进行验证key的枚举类
     * @return 返回被分类后的key。sql key，非 sql key
     * @param <T> 枚举类
     */
    <T extends Enum<T>> ResultDTO detachTemplateString(List<String> keys, Class<T> verifyClass);

    /**
     *
     * @param beInput 输入的字符串
     * @return 把满足匹配的关键key以数组的形式return出来
     */
    List<String> extractStringsInBraces(String beInput);
    /**
     *
     * @param relativeWords key: sql key, value: sql value
     * @param beInput 需要替换的字符串
     */
    String replaceSqlKey(Map<String, Object> relativeWords, String beInput);
}
