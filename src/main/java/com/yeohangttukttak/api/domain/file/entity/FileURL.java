package com.yeohangttukttak.api.domain.file.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FileURL {

    private static String localHost;

    @Value("${host.storage.local}")
    public void setLocalPath(String localHost) {
        FileURL.localHost = localHost;
    }

    public static String create(StorageType storageType, String... tokens) {

        StringBuilder sb = new StringBuilder();

        if (storageType == StorageType.LOCAL) {
            sb.append(localHost);
        }

        Arrays.stream(tokens).forEach(sb::append);
        return sb.toString();
    }

}
