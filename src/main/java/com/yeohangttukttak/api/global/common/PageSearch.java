package com.yeohangttukttak.api.global.common;

import lombok.Data;

@Data
public class PageSearch {

    private int page;

    private int pageSize;

    public int getOffset() {
        return (page - 1) * pageSize;
    }

}