package com.drsg.gochat.v1.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * @author YXs
 */
@Component
@ConfigurationProperties(prefix = "upload.images")
public class ImageUploadUtils {
    private String dir;
    private String baseUrl;

    public String storeImage(MultipartFile image) throws IOException {
        StringBuilder filePath = new StringBuilder(dir);
        String imageType = image.getContentType();
        String suffix;
        switch (Objects.requireNonNull(imageType)) {
            case "image/png":
                suffix = ".png";
                break;
            case "image/gif":
                suffix = ".gif";
                break;
            default:
                suffix = ".jpg";
        }
        String fileName = UUID.randomUUID().toString() + suffix;
        String dirSymbol = "/";
        if (!dir.endsWith(dirSymbol)) {
            filePath.append(dirSymbol);
        }
        filePath.append(fileName);
        Path imagePath = Paths.get(filePath.toString());
        image.transferTo(imagePath);
        StringBuilder imageUrl = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith(dirSymbol)) {
            imageUrl.append(dirSymbol);
        }
        imageUrl.append(fileName);
        return imageUrl.toString();
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
