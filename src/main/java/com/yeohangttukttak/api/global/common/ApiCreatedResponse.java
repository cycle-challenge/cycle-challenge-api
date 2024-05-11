package com.yeohangttukttak.api.global.common;

import lombok.Data;

@Data
public class ApiCreatedResponse<T, E> {

    private String type;
    private E id;

    public ApiCreatedResponse(Class<T> type, E id) {
        this.type = type.getSimpleName();
        this.id = id;
    }
}
