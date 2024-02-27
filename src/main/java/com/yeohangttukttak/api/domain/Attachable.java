package com.yeohangttukttak.api.domain;

import com.yeohangttukttak.api.domain.file.File;

import java.util.List;

public interface Attachable {

    public Long getId();

    public List<File> getFiles();

}
