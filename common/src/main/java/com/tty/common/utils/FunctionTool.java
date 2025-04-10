package com.tty.common.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FunctionTool {

    public void setResponse(HttpServletResponse response, int code, int dataCode, String message) {
        response.setStatus(code);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(JSONObject.toJSONString(JsonResult.ERROR(dataCode, message)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param xj 是否保留末尾的 ":"
     * @param keys keys
     * @return 返回构建的Redis key
     */
    public String buildRedisKey(boolean xj, Object... keys) {
        StringBuilder sb = new StringBuilder();
        for (Object key : keys) {
            String a = key.toString();
            sb.append(a).append(":");
        }
        if (!xj) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
