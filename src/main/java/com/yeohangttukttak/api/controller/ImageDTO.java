package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.file.File;
import lombok.Getter;

@Getter
public class ImageDTO {

    private Long id;

    private String url;

    public ImageDTO (File file) {
        this.id = file.getId();
        this.url = file.getUrl();
    }

}
