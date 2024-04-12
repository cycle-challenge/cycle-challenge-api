package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    ApiErrorCode errorCode;

    public ApiException(ApiErrorCode errorCode) {
        super(errorCode.name());
    }
}
