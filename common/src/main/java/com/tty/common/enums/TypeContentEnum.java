package com.tty.common.enums;

import lombok.Getter;

@Getter
public enum TypeContentEnum {
    HTML("text/html"),
    JSON("application/json"),
    XML("application/xml"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    PNG("image/png"),
    GIF("image/gif"),
    CSS("text/css"),
    JAVASCRIPT("application/javascript"),
    PDF("application/pdf"),
    MPEG("video/mpeg"),
    MP3("audio/mpeg"),
    MP4("video/mp4"),
    OTF("application/x-font-otf"),
    TTF("application/x-font-ttf"),
    SVG("image/svg+xml"),
    WEBP("image/webp"),
    AVI("video/x-msvideo"),
    QUICKTIME("video/quicktime"),
    WORD("application/msword"),
    EXCEL("application/vnd.ms-excel"),
    POWERPOINT("application/vnd.ms-powerpoint"),
    TEXT("text/plain"),
    CSV("text/csv"),
    RTF("application/rtf"),
    ZIP("application/zip"),
    RAR("application/x-rar-compressed"),
    SEVEN_Z("application/x-7z-compressed");

    private final String value;

    TypeContentEnum(String value) {
        this.value = value;
    }
}
