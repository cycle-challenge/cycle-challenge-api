package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiErrorCode {

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST.value()),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND.value());

    private final int status;

}
