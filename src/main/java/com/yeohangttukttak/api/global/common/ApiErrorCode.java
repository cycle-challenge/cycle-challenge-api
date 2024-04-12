package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiErrorCode {

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, null),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "email"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "nickname"),
    PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "password"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, null),
    SIGN_IN_FAILED(HttpStatus.UNAUTHORIZED, null),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, null);

    private final HttpStatus status;
    private final String target;

}
