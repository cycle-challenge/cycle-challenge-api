package com.yeohangttukttak.api.domain.file.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileURL {

    private static String localPath;

    @Value("${path.storage.local}")
    public void setLocalPath(String localPath) {
        FileURL.localPath = localPath;
    }

    public static String create(StorageType storageType,
                                String path, String name) {
        final StringBuilder sb = new StringBuilder();

        if (storageType == StorageType.LOCAL) {
            sb.append(localPath);
        }

        return sb.append(path)
                .append(name)
                .toString();
    }

}
