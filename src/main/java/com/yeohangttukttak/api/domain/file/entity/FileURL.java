package com.yeohangttukttak.api.domain.file.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FileURL {

    private static String localHost;

    @Value("${host.storage.local}")
    public void setLocalPath(String localHost) {
        FileURL.localHost = localHost;
    }

    public static String create(StorageType storageType,
                                String path, String name) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .newInstance()
                .scheme("https");

        if (storageType == StorageType.LOCAL) {
            builder.host(localHost);
        }

        return builder.path(path)
                .path(name)
                .build().toString();
    }

}
