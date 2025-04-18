package com.tty.system.filter;

import com.tty.common.enums.EncodeType;
import com.tty.common.utils.CompressionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(Integer.MIN_VALUE)
public class ZstdCompressionFilter implements Filter {

    @Resource
    private CompressionUtils compressionUtils;
    @Value("${server.compression.mime-types}")
    private String[] mimeTypes;
    @Value("${server.compression.min-response-size}")
    private Integer minResponseSize;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String acceptEncoding = ((HttpServletRequest) request).getHeader("Accept-Encoding");
        if(!this.hasAcceptedMimeType((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }

        if (acceptEncoding == null || acceptEncoding.isEmpty()) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Accept-Encoding header");
            return;
        }

        EncodeType encoding = chooseEncoding(acceptEncoding);

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(httpResponse);
        chain.doFilter(request, wrapper);
        byte[] contentAsByteArray = wrapper.getContentAsByteArray();

        if (contentAsByteArray.length < this.minResponseSize) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.setHeader("Content-Encoding", encoding.getKey());
        switch (encoding) {
            case GZIP -> httpResponse.getOutputStream().write(this.compressionUtils.toGzip(contentAsByteArray));
            case ZSTD -> httpResponse.getOutputStream().write(this.compressionUtils.toZstd(contentAsByteArray));
        }

    }
    private boolean hasAcceptedMimeType(HttpServletRequest httpRequest) {
        String acceptHeader = httpRequest.getHeader("Accept");
        if (acceptHeader == null) {
            return false;
        }
        String[] acceptTypes = acceptHeader.split("\\s*,\\s*");
        for (String type : acceptTypes) {
            String cleanedType = type.split(";", 2)[0].trim();
            if (Arrays.asList(this.mimeTypes).contains(cleanedType)) {
                return true;
            }
        }
        return false;
    }
    private EncodeType chooseEncoding(String acceptEncoding) {
        List<String> encodings = Arrays.stream(acceptEncoding.split(",")).map(i -> i.replace(" ", "")).toList();
        if(encodings.contains(EncodeType.ZSTD.getKey())) return EncodeType.ZSTD;
        return EncodeType.GZIP;
    }
}
