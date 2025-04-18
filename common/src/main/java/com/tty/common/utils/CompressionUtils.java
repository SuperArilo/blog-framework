package com.tty.common.utils;

import com.github.luben.zstd.ZstdOutputStream;
import com.tty.common.enums.EncodeType;
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

    public byte[] decompress(byte[] data, EncodeType type) throws IOException {

        if (type == null) return data;
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        InputStream uncompressedStream = switch (type) {
            case GZIP -> new GZIPInputStream(bis);
            case ZSTD -> new ZstdCompressorInputStream(bis);
            case DEFAULT -> new DeflaterInputStream(bis);
            case NONE -> bis;
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

    public byte[] toZstd(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ZstdOutputStream zstdOutputStream = new ZstdOutputStream(bos)) {
            zstdOutputStream.write(data);
        }
        return bos.toByteArray();
    }
}