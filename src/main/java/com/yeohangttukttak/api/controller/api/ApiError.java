package com.yeohangttukttak.api.controller.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {

    private int status;

    private String code;

    private String message;

    public ApiError(ApiErrorCode code, String message) {
        this.status = code.getStatus();
        this.code = code.name();
        this.message = message;
    }

}
