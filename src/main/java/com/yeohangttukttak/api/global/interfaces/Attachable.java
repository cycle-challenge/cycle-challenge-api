package com.yeohangttukttak.api.global.interfaces;

import com.yeohangttukttak.api.domain.file.entity.File;

import java.util.List;

public interface Attachable {

    public Long getId();

    public List<File> getFiles();

}
