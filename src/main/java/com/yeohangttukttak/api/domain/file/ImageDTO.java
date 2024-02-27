package com.yeohangttukttak.api.domain.file;

import com.yeohangttukttak.api.domain.file.File;
import lombok.Data;
import lombok.Getter;

@Data
public class ImageDTO {

    private Long id;

    private String url;

    public ImageDTO (File file) {
        this.id = file.getId();
        this.url = file.getUrl();
    }

}
