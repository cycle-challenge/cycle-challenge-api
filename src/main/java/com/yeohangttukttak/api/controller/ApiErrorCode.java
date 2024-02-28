package com.yeohangttukttak.api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiErrorCode {

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST.value());

    private final int status;

}
