package com.yeohangttukttak.api.global.interfaces;

import com.yeohangttukttak.api.domain.file.entity.Image;

import java.util.List;

public interface Attachable {

    public Long getId();

    public List<Image> getFiles();

}
