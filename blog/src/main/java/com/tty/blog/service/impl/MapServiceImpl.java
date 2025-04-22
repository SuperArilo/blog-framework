package com.tty.blog.service.impl;

import com.tty.common.mapper.MapMapper;
import com.tty.blog.service.MapService;
import com.tty.common.vo.map.MapRawConfigData;
import com.tty.common.enums.EncodeType;
import com.tty.common.utils.CompressionUtils;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class MapServiceImpl implements MapService {

    @Resource
    private MapMapper mapMapper;
    @Resource
    private CompressionUtils compressionUtils;
    private static final HttpHeaders DAY_HEADERS;
    static {
        DAY_HEADERS = new HttpHeaders();
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic();
        DAY_HEADERS.setCacheControl(cacheControl.getHeaderValue());
        DAY_HEADERS.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public ResponseEntity<byte[]> getConfigOrStatus(String mapId, String fileName) {
        MapRawConfigData configRawData = this.mapMapper.getConfigRawData(mapId, fileName);
        if (configRawData != null) {
            try {
                return ResponseEntity.ok(this.compressionUtils.decompress(configRawData.getData(), EncodeType.valueOf(configRawData.getKey().replace("bluemap:", "").toUpperCase())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<byte[]> getTileData(String mapId, int lod, int x, int z) {
        String key = lod == 0 ? "hires":"lowres/" + lod;
        MapRawConfigData tilesRawData = this.mapMapper.getTilesRawData(mapId, key, x, z);
        if (tilesRawData != null) {
            try {
                byte[] decompress = this.compressionUtils.decompress(tilesRawData.getData(), EncodeType.valueOf(tilesRawData.getKey().replace("bluemap:", "").toUpperCase()));
                if (lod == 0) {
                    return new ResponseEntity<>(decompress, DAY_HEADERS, HttpStatus.OK);
                }
                return new ResponseEntity<>(decompress, DAY_HEADERS, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.noContent().build();
    }
}
