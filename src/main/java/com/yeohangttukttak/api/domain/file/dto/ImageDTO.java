package com.yeohangttukttak.api.domain.file.dto;

import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.file.entity.FileURL;
import lombok.Data;

@Data
public class ImageDTO {

    private Long id;

    private String url;

    private String mimeType;

    public ImageDTO (File file) {
        this.id = file.getId();
        this.url = FileURL.create(file.getStorageType(),
                          file.getPath(), file.getName());
        this.mimeType = file.getMimeType();
    }

}
