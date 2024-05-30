package com.yeohangttukttak.api.global.common;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    ApiErrorCode errorCode;
    String[] args;

    public ApiException(ApiErrorCode errorCode, String... args) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.args = args;
    }
}
