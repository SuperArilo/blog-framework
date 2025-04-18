package com.tty.system.utils;

import com.tty.common.enums.TypeContentEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class BunnyUtil {

    @Value("${cdn.bunny.region}")
    private Region region;
    @Value("${cdn.bunny.zone}")
    private String zone;
    @Value("${cdn.bunny.password}")
    private String accessKey;

    public boolean upload(MultipartFile file, String fileName, String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.region.getUrl() + "/" + this.zone + (path.equals("/") ? "":path) + "/" + fileName))
                    .header("content-type", "application/octet-stream")
                    .header("AccessKey", this.accessKey)
                    .method("PUT", HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                    .build();
            try(HttpClient httpClient = HttpClient.newHttpClient()) {
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
    public void delete(String fileName, String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.region.getUrl() + "/" + this.zone + (path.equals("/") ? "":path) + "/" + fileName))
                .header("AccessKey", this.accessKey)
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .build();
        try(HttpClient httpClient = HttpClient.newHttpClient()) {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] ListFiles(String path) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.region.getUrl() + "/" + this.zone + path))
            .header("accept", "*/*")
            .header("AccessKey", this.accessKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        try(HttpClient httpClient = HttpClient.newHttpClient()) {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public enum Region {
        DE("storage.bunnycdn.com"),
        UK("uk.storage.bunnycdn.com"),
        US_NY("ny.storage.bunnycdn.com"),
        US_LA("la.storage.bunnycdn.com"),
        SG("sg.storage.bunnycdn.com"),
        SE("se.storage.bunnycdn.com"),
        BR("br.storage.bunnycdn.com"),
        SA("jh.storage.bunnycdn.com");

        private final String url;

        Region(String region) {
            this.url = region;
        }

        public String getUrl() {
            return "https://" + this.url;
        }
    }
}
