package com.tty.common.utils;

import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Component
public class CompressionUtils {

    public byte[] decompress(byte[] data, String algorithm) throws IOException {

        if (algorithm == null) return data;
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        InputStream uncompressedStream = switch (algorithm) {
            case "gzip" -> new GZIPInputStream(bis);
            case "zstd" -> new ZstdCompressorInputStream(bis);
            case "default" -> new DeflaterInputStream(bis);
            case "none" -> bis;
            default -> throw new IllegalStateException("Unexpected value: " + algorithm);
        };

        return uncompressedStream.readAllBytes();
    }

    public byte[] toGzip(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(data);
        }
        return bos.toByteArray();
    }
}