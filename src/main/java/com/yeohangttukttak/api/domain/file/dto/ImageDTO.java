package com.yeohangttukttak.api.domain.file.dto;

import com.yeohangttukttak.api.domain.file.entity.FileURL;
import com.yeohangttukttak.api.domain.file.entity.Image;
import lombok.Data;

@Data
public class ImageDTO {

    private Long id;

    private String small;

    private String medium;

    private String large;

    public ImageDTO (Image image) {
        this.id = image.getId();

        this.small = FileURL.create(image.getStorageType(),
                "/_144", image.getPath(), image.getName());
        this.medium = FileURL.create(image.getStorageType(),
                "/_360", image.getPath(), image.getName());
        this.large = FileURL.create(image.getStorageType(),
                "/_720", image.getPath(), image.getName());
    }

}
