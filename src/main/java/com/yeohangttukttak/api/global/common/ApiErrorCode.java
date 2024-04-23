package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiErrorCode {

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, null),
    PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "password"),

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "email"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "nickname"),
    DUPLICATED_BOOKMARK(HttpStatus.CONFLICT, null),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, null),
    BOOKMARK_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, null),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, null),

    SIGN_IN_FAILED(HttpStatus.UNAUTHORIZED, null),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "verificationCode"),
    AUTHORIZATION_REQUIRED(HttpStatus.UNAUTHORIZED, null),
    INVALID_AUTHORIZATION(HttpStatus.UNAUTHORIZED, null),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, null);

    private final HttpStatus status;
    private final String target;

}
