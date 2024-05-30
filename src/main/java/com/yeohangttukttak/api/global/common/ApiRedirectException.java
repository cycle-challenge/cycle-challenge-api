package com.yeohangttukttak.api.global.common;

import lombok.Getter;

@Getter
public class ApiRedirectException extends RuntimeException {

    String redirectUrl;
    ApiErrorCode errorCode;
    String[] args;

    public ApiRedirectException(String redirectUrl, ApiErrorCode errorCode, String... args) {
        super(errorCode.name());

        this.redirectUrl = redirectUrl;
        this.errorCode = errorCode;
        this.args = args;
    }
}
