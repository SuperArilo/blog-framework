package com.tty.blog.service;

import org.springframework.http.ResponseEntity;

public interface MapService {
    ResponseEntity<byte[]> getConfigOrStatus(String mapId, String fileName);
    ResponseEntity<byte[]> getTileData(String mapId, int lod, int x, int z);
}
