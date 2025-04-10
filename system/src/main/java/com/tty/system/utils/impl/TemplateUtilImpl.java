package com.tty.system.utils.impl;

import com.tty.system.entity.ResultDTO;
import com.tty.system.mapper.SysUserMeansMapper;
import com.tty.system.utils.TemplateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateUtilImpl implements TemplateUtil {
    @Resource
    SysUserMeansMapper sysUserMeansMapper;
    @Override
    public Map<String, Object> getParaphraseKeys(List<String> keys, Long uid) {
        if (keys.isEmpty()) return null;
        return this.sysUserMeansMapper.dynamicMatching(keys, uid);
    }

    @Override
    public <T extends Enum<T>> ResultDTO detachTemplateString(List<String> keys, Class<T> verifyClass) {
        ResultDTO dto = new ResultDTO();
        List<String> passKeys = new ArrayList<>();
        List<String> interceptKeys = new ArrayList<>();
        keys.forEach(e -> {
            try {
                Enum.valueOf(verifyClass, Character.toUpperCase(e.charAt(0)) + e.substring(1));
                passKeys.add(e);
            } catch (Exception exception) {
                interceptKeys.add(e);
            }
        });
        dto.setSqlKey(passKeys);
        dto.setInterceptKeys(interceptKeys);
        return dto;
    }

    @Override
    public List<String> extractStringsInBraces(String beInput) {
        List<String> extractedStrings = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(.*?)}").matcher(beInput);
        while (matcher.find()) {
            String extractedString = matcher.group(1);
            extractedStrings.add(extractedString);
        }
        return extractedStrings;
    }

    @Override
    public String replaceSqlKey(Map<String, Object> relativeWords, String beInput) {
        if (relativeWords == null || relativeWords.isEmpty()) return null;
        for (Map.Entry<String, Object> entity: relativeWords.entrySet()) {
            beInput = beInput.replace("{" + entity.getKey() + "}", entity.getValue().toString());
        }
        return beInput;
    }
}
