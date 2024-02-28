package com.yeohangttukttak.api.controller.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {

    private int status;

    private String code;

    private String message;

    private String target;

    public ApiError(ApiErrorCode code, String message, String target) {
        this.status = code.getStatus();
        this.code = code.name();
        this.message = message;
        this.target = target;
    }

}
