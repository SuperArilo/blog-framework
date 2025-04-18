package com.tty.system.filter;

import com.tty.common.enums.EncodeType;
import com.tty.common.utils.CompressionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class ZstdCompressionFilter implements Filter {

    @Resource
    private CompressionUtils compressionUtils;
    @Value("${server.compression.min-response-size}")
    private Integer minResponseSize;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String acceptEncoding = ((HttpServletRequest) request).getHeader("Accept-Encoding");

        if (acceptEncoding == null || acceptEncoding.isEmpty()) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Accept-Encoding header");
            return;
        }


        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(httpResponse);
        chain.doFilter(request, wrapper);
        byte[] responseBody = wrapper.getContentAsByteArray();

        if (responseBody.length < this.minResponseSize || this.isAlreadyCompressed(responseBody)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            EncodeType encoding = chooseEncoding(acceptEncoding);
            byte[] compressedData = this.compressData(responseBody, encoding);

            // 8. 设置响应头（注意顺序）
            httpResponse.resetBuffer();
            httpResponse.setHeader("Content-Encoding", encoding.getKey());
            httpResponse.setContentLength(compressedData.length);

            // 9. 写入压缩数据
            try (OutputStream out = httpResponse.getOutputStream()) {
                out.write(compressedData);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            wrapper.copyBodyToResponse();
        }

    }

    private EncodeType chooseEncoding(String acceptEncoding) {
        List<String> encodings = Arrays.stream(acceptEncoding.split(",")).map(i -> i.replace(" ", "")).toList();
        if(encodings.contains(EncodeType.ZSTD.getKey())) return EncodeType.ZSTD;
        return EncodeType.GZIP;
    }

    private byte[] compressData(byte[] data, EncodeType encoding) throws IOException {
        return switch (encoding) {
            case ZSTD -> compressionUtils.toZstd(data);
            case GZIP -> compressionUtils.toGzip(data);
            default -> throw new IllegalArgumentException("Unsupported encoding");
        };
    }

    private boolean isAlreadyCompressed(byte[] data) {
        if (data.length < 2) return false;
        return (data[0] == (byte) 0x1f && data[1] == (byte) 0x8b) ||
                (data[0] == (byte) 0xFD && data[1] == (byte) 0x2F &&
                        data[2] == (byte) 0xB5 && data[3] == (byte) 0x28);
    }
}
