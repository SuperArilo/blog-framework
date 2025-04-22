package com.tty.system.filter;

import com.tty.common.enums.EncodeType;
import com.tty.common.utils.CompressionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
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
            wrapper.copyBodyToResponse();
            return;
        }

        try {
            EncodeType encoding = chooseEncoding(acceptEncoding);
            byte[] compressedData = this.compressData(responseBody, encoding);

            httpResponse.resetBuffer();
            httpResponse.setHeader("Content-Encoding", encoding.getKey());
            httpResponse.setContentLength(compressedData.length);

            try (OutputStream out = httpResponse.getOutputStream()) {
                out.write(compressedData);
                out.flush();
            }
        } catch (ClientAbortException e) {
            log.info("Client aborted connection during compression");
        } finally {
            wrapper.copyBodyToResponse();
        }

    }

    private EncodeType chooseEncoding(String acceptEncoding) {
        List<String> encodings = Arrays.stream(acceptEncoding.split(",")).map(i -> i.replace(" ", "")).toList();
        if(encodings.contains(EncodeType.ZSTD.getKey())) return EncodeType.ZSTD;
        return EncodeType.GZIP;
    }

    private byte[] compressData(byte[] data, EncodeType encoding) {
        return this.compressionUtils.compress(data, encoding);
    }

    private boolean isAlreadyCompressed(byte[] data) {
        if (data.length < 2) return false;
        return (data[0] == (byte) 0x1f && data[1] == (byte) 0x8b) ||
                (data[0] == (byte) 0xFD && data[1] == (byte) 0x2F &&
                        data[2] == (byte) 0xB5 && data[3] == (byte) 0x28);
    }
}
