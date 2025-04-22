package com.tty.common.utils;

import com.github.luben.zstd.ZstdOutputStream;
import com.tty.common.enums.EncodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
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

    public byte[] compress(byte[] data, EncodeType type) {
        if (type == null || data == null || data.length == 0) {
            return data;
        }

        try {
            return switch (type) {
                case GZIP -> compressWithStream(data, GZIPOutputStream::new);
                case ZSTD -> compressWithStream(data, ZstdOutputStream::new);
                case DEFAULT -> compressWithStream(data, DeflaterOutputStream::new);
                case NONE -> data;
            };
        } catch (Exception e) {
            log.error("Compression failed for type: {}", type, e);
            return data;
        }
    }

    private byte[] compressWithStream(byte[] data, IOStreamFactory streamFactory) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (OutputStream compressionStream = streamFactory.create(bos)) {
            compressionStream.write(data);
        }
        return bos.toByteArray();
    }

    @FunctionalInterface
    private interface IOStreamFactory {
        OutputStream create(OutputStream out) throws IOException;
    }
}