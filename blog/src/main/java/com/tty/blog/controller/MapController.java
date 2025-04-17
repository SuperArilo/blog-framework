package com.tty.blog.controller;

import com.tty.blog.service.MapService;
import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/maps")
public class MapController {

    @Resource
    private MapService mapService;
    private static final Pattern X_Z_PATTERN = Pattern.compile("x([+-]?\\d+).*?z([+-]?\\d+)");

    @GetMapping("/{mapId}/tiles/{lod}/**")
    public ResponseEntity<?> getTile(@PathVariable String mapId, @PathVariable int lod, HttpServletRequest request) {

        String remainingPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String normalizedPath = remainingPath.replace("/", "");
        Matcher matcher = X_Z_PATTERN.matcher(normalizedPath);

        if (!matcher.find()) {
            log.warn("Invalid x/z parameters in path: {}", remainingPath);
            return ResponseEntity.badRequest().body("Invalid x/z parameters format");
        }

        try {
            return mapService.getTileData(mapId, lod, Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } catch (NumberFormatException e) {
            log.error("Number format exception for x/z in path: {}", remainingPath, e);
            return ResponseEntity.badRequest().body("x/z values out of integer range");
        }
    }

    @GetMapping("/{mapId}/{fileName}.json")
    public ResponseEntity<byte[]> getConfigFile(@PathVariable("mapId") String mapId,
                                                @PathVariable("fileName") String fileName) {
        return this.mapService.getConfigOrStatus(mapId, fileName);
    }
    @GetMapping("/{mapId}/live/{fileName}.json")
    public ResponseEntity<byte[]> getMapLiveStatus(@PathVariable("mapId") String mapId,
                                                   @PathVariable("fileName") String fileName) {
        return this.mapService.getConfigOrStatus(mapId, fileName);
    }
    @GetMapping("/{mapId}/assets/playerheads/{uuid}")
    public ResponseEntity<byte[]> getPlayerHead(@PathVariable("uuid") String uuid_png, @PathVariable String mapId) {
        return this.mapService.getConfigOrStatus(mapId, "asset/playerheads/" + uuid_png);
    }

}
