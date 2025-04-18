package com.tty.system.controller;

import com.tty.common.enums.TypeContentEnum;
import com.tty.system.utils.BunnyUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/cdn/path")
public class BunnyCDNController {

    @Resource
    BunnyUtil bunnyUtil;
    private static final Pattern pattern = Pattern.compile("(/path)(/.*)");

    @GetMapping("/**")
    public ResponseEntity<byte[]> get(HttpServletRequest request) {
        Matcher matcher = pattern.matcher(request.getServletPath());
        if (matcher.find()) {
            String group = matcher.group(2);
            TypeContentEnum fileEndType = this.getFileEndType(group);
            if (fileEndType == null) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(fileEndType.getValue()));
            return new ResponseEntity<>(this.bunnyUtil.ListFiles(group), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
    private TypeContentEnum getFileEndType(String path) {
        Matcher matcher = Pattern.compile(".*[./]([^./]+)$").matcher(path);
        String extension = matcher.find() ? matcher.group(1) : "";
        try {
            return TypeContentEnum.valueOf(extension.toUpperCase());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
